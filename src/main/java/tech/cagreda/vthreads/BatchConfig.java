package tech.cagreda.vthreads;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import tech.cagreda.adapter.out.db.mongo.model.PaymentRequestModel;
import tech.cagreda.application.ports.out.ReferenceTablePortOut;

@Configuration
public class BatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final TaskExecutor virtualThreadTaskExecutor;
  private final ReferenceTablePortOut referenceTablePortOut;

  public BatchConfig(
      JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      TaskExecutor virtualThreadTaskExecutor,
      ReferenceTablePortOut referenceTablePortOut) {
    this.jobRepository = jobRepository;
    this.transactionManager = transactionManager;
    this.virtualThreadTaskExecutor = virtualThreadTaskExecutor;
    this.referenceTablePortOut = referenceTablePortOut;
  }

  @Bean
  public Job fileProcessingJob(Step processFileStep) {
    return new JobBuilder("fileProcessingJob", jobRepository).start(processFileStep).build();
  }

  @Bean
  public Step processFileStep(
      ItemReader<TxInfAndSts> reader,
      // ItemReader<Document> reader,
      ItemProcessor<TxInfAndSts, String> processor,
      // ItemProcessor<Document, String> processor,
      ItemWriter<String> writer) {

    return new StepBuilder("processFileStep", jobRepository)
        .<TxInfAndSts, String>chunk(1, transactionManager)
        // .<Document, String>chunk(1000, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .taskExecutor(virtualThreadTaskExecutor)
        .build(); // Use virtual thread executor
  }

  @Bean
  @StepScope
  public StaxEventItemReader<TxInfAndSts> reader() {

    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setClassesToBeBound(TxInfAndSts.class);

    return new StaxEventItemReaderBuilder<TxInfAndSts>()
        .name("itemReader")
        .resource(new ClassPathResource("CANONICALPain002V12.xml"))
        .addFragmentRootElements("TxInfAndSts")
        .unmarshaller(marshaller)
        .build();
  }

  // ItemProcessor Bean (custom implementation)
  @Bean
  @StepScope
  public ItemProcessor<TxInfAndSts, String> processor() {
    // public ItemProcessor<Document, String> processor() {
    return line -> {
      Optional<PaymentRequestModel> result =
          referenceTablePortOut.findMessageId(line.getOrgnlEndToEndId());

      return result
          .map(paymentRequestModel -> line + "," + paymentRequestModel.bankFileName())
          .orElseGet(() -> line + ",,");
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
