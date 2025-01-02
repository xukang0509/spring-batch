package com.shanhai.batch._15_job_start_restful;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author xk
 * @since 2024-12-12 19:21
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            Map<String, Object> parameters = chunkContext.getStepContext().getJobParameters();
            System.out.println("Hello SpringBatch restful...." + parameters.get("name"));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step").tasklet(tasklet()).build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("start-restful-job").start(step())
                .incrementer(new RunIdIncrementer()).build();
    }
}
