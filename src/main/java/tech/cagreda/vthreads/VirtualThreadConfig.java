package tech.cagreda.vthreads;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class VirtualThreadConfig {

    @Bean
    public TaskExecutor virtualThreadTaskExecutor() {
        ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        return new TaskExecutorAdapter(executorService);
    }
}

