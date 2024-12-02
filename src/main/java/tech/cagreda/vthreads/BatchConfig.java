package tech.cagreda.vthreads;

import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;
import tech.cagreda.application.ports.out.ReferenceTablePortOut;
import tech.cagreda.eventreader.CustomXmlEventReader;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

  private final JobRepository jobRepository;
  private final PlatformTransactionManager transactionManager;
  private final TaskExecutor virtualThreadTaskExecutor;
  private final ReferenceTablePortOut referenceTablePortOut;

  @Bean
  public Step step1(
      CustomXmlEventReader reader,
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
        .taskExecutor(taskExecutor()) // Enables multi-threaded execution
        .build(); // Use virtual thread executor
  }

  @Bean
  public TaskExecutor taskExecutor() {
    SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
    executor.setConcurrencyLimit(4); // Adjust the number of concurrent threads
    return executor;
  }

  @Bean
  public Job fileProcessingJob(Step step1) {
    return new JobBuilder("fileProcessingJob", jobRepository).start(step1).build();
  }

  @Bean(destroyMethod = "close")
  @StepScope
  public CustomXmlEventReader reader(@Value("#{jobParameters['filePath']}") String filePath)
      throws Exception {
    return new CustomXmlEventReader(new ClassPathResource(filePath).getFile().getPath());
  }

  /*
    @Bean
    public Jaxb2Marshaller marshaller() {
      Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
      marshaller.setClassesToBeBound(TxInfAndSts.class);

      return marshaller;
    }
  */

  /*  @Bean
    @StepScope
    public StaxEventItemReader<TxInfAndSts> reader()  {


      StaxEventItemReader<TxInfAndSts> readerStax = new StaxEventItemReaderBuilder<TxInfAndSts>()
              .name("itemReader")
              .unmarshaller(marshaller())
              .resource(new ClassPathResource("test.xml"))
              .addFragmentRootElements("TxInfAndSts")
              .build();
    //  readerStax.open(new ExecutionContext());


  */
  /*    boolean hasNext = true;

  while (hasNext) {
    TxInfAndSts read = null;
    try {
      read = readerStax.read();
    } catch (Exception e) {
     e.printStackTrace();
    }
    if(read == null) {
      hasNext = false;
    }else {
      System.out.println(read);
    }
  }*/
  /*


    return  readerStax;
  }*/

  // ItemProcessor Bean (custom implementation)
  @Bean
  public ItemProcessor<TxInfAndSts, String> processor() {
    // public ItemProcessor<Document, String> processor() {
    return line -> {
      /*      Optional<PaymentRequestModel> result =
          referenceTablePortOut.findMessageId(line.getOrgnlEndToEndId());

      return result
          .map(paymentRequestModel -> "," + paymentRequestModel.bankFileName())
          .orElseGet(() -> line.getOrgnlEndToEndId()+" - ");*/
      return line.getOrgnlEndToEndId();
    };
  }

  @Bean
  public ItemWriter<String> itemWriter() {
    return items -> {
      System.out.println("Processing chunk of size: " + items.size());
      items.forEach(System.out::println);
    };
  }
  // ItemWriter Bean
  /*  @Bean
  @StepScope
  public FlatFileItemWriter<String> writer(@Value("${output.file.path}") String outputFile) {

    System.out.println("===> writing");
    return new FlatFileItemWriterBuilder<String>()
        .name("outputFileWriter")
        .resource(new FileSystemResource(outputFile))
        .lineAggregator(line -> line)
        .build();
  }*/
}
