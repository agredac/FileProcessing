package tech.cagreda.vthreads;


import org.bson.Document;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final TaskExecutor virtualThreadTaskExecutor;

    public BatchConfig(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       TaskExecutor virtualThreadTaskExecutor) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.virtualThreadTaskExecutor = virtualThreadTaskExecutor;
    }

    @Bean
    public Job fileProcessingJob(Step processFileStep) {
        return new JobBuilder("fileProcessingJob", jobRepository)
                .start(processFileStep)
                .build();
    }

    @Bean
    public Step processFileStep(ItemReader<String> reader,
                                ItemProcessor<String, String> processor,
                                ItemWriter<String> writer) {

        StepBuilder stepBuilder = new StepBuilder("processFileStep", jobRepository);
        SimpleStepBuilder<String, String> simpleStepBuilder = stepBuilder
                .<String, String>chunk(1000, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(virtualThreadTaskExecutor); // Use virtual thread executor

        return simpleStepBuilder.build();
    }

    // ItemReader Bean
    @Bean
    @StepScope
    public FlatFileItemReader<String> reader(@Value("${input.file.path}") String inputFile) {
        FlatFileItemReader<String> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(inputFile));
        reader.setLineMapper(new PassThroughLineMapper()); // Directly passes each line as a String
        return reader;
    }

    // ItemProcessor Bean (custom implementation)
    @Bean
    @StepScope
    public ItemProcessor<String, String> processor(MongoTemplate mongoTemplate) {
        return line -> {
            String[] fields = line.split(",");
            String id = fields[0]; // Assuming ID is in the first column


            Query query = new Query();
            query.addCriteria(Criteria.where("id").is(id));

            Document result = mongoTemplate.findOne(query, Document.class, "yourCollection");  // Replace "yourCollection" with the actual collection name


            if (result != null) {
                return line + "," + result.getString("mongoField1") + "," + result.getString("mongoField2");
            } else {
                return line + ",,"; // If no MongoDB data found
            }
        };
    }

    // ItemWriter Bean
    @Bean
    @StepScope
    public FlatFileItemWriter<String> writer(@Value("${output.file.path}") String outputFile) {
        return new FlatFileItemWriterBuilder<String>()
                .name("outputFileWriter")
                .resource(new FileSystemResource(outputFile))
                .lineAggregator(line -> line)
                .build();
    }
}
