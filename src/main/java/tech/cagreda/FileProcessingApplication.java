package tech.cagreda;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FileProcessingApplication implements CommandLineRunner {


    private final JobLauncher jobLauncher;

    private final Job fileProcessingJob;

    public FileProcessingApplication(JobLauncher jobLauncher, Job fileProcessingJob) {
        this.jobLauncher = jobLauncher;
        this.fileProcessingJob = fileProcessingJob;
    }

    public static void main(String[] args) {
        SpringApplication.run(FileProcessingApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        jobLauncher.run(fileProcessingJob, new org.springframework.batch.core.JobParameters());
    }
}

