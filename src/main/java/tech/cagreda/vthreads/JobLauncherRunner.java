package tech.cagreda.vthreads;


import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobLauncherRunner {


    private final JobLauncher jobLauncher;

    private final Job job;  // Inject the job to be run

    @Bean
    public ApplicationRunner runBatchJob() {
        return args -> {
            String filePath = "test.xml";  // Replace with your actual file path
            System.out.println("Starting batch job with file path: " + filePath);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", filePath)
                    .toJobParameters();

            try {
                JobExecution jobExecution = jobLauncher.run(job, jobParameters);
                System.out.println("Job Status: " + jobExecution.getStatus());
            } catch (Exception e) {
                System.err.println("Failed to start batch job: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
}
