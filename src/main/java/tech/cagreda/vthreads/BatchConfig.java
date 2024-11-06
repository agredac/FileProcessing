package tech.cagreda.vthreads;

import com.latam.ecs.canonical.model.paymentresponses.Document;
import java.util.Optional;

import com.latam.ecs.canonical.model.paymentresponses.OriginalPaymentInstruction40;
import com.latam.ecs.canonical.model.paymentresponses.PaymentTransaction129;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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
      ItemReader<PaymentTransaction129> reader,
      ItemProcessor<PaymentTransaction129, String> processor,
      ItemWriter<String> writer) {

    return new StepBuilder("processFileStep", jobRepository)
        .<PaymentTransaction129, String>chunk(1000, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .taskExecutor(virtualThreadTaskExecutor)
        .build(); // Use virtual thread executor
  }

  @Bean
  @StepScope
  public StaxEventItemReader<PaymentTransaction129> reader() {
    StaxEventItemReader<PaymentTransaction129> reader = new StaxEventItemReader<>();
    reader.setResource(new ClassPathResource("CANONICALPain002V12.xml"));
    reader.setFragmentRootElementName("TxInfAndSts"); // Root element for each record

    Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
    marshaller.setClassesToBeBound(PaymentTransaction129.class); // Bind XML to Employee class
    reader.setUnmarshaller(marshaller);

    return reader;
  }

  // ItemProcessor Bean (custom implementation)
  @Bean
  @StepScope
  public ItemProcessor<PaymentTransaction129, String> processor() {
    return line -> {

      Optional<PaymentRequestModel> result = referenceTablePortOut.findMessageId(
              line.getOrgnlEndToEndId());

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
