package com.shanhai.batch._06_context;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author xk
 * @since 2024-12-12 13:36
 */
@SpringBootApplication
@EnableBatchProcessing
public class ExecutionContextJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet1() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                    throws Exception {
                // 作业
                ExecutionContext jobEc = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                jobEc.put("key-step1-job", "value-step1-job");
                System.err.println("-".repeat(40));
                // 步骤
                ExecutionContext stepEc = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                stepEc.put("key-step1-step", "value-step1-step");
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Tasklet tasklet2() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                // 作业
                ExecutionContext jobEc = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
                System.err.println("key-step1-job = " + jobEc.get("key-step1-job"));
                System.err.println("=".repeat(40));
                // 步骤
                ExecutionContext stepEc = chunkContext.getStepContext().getStepExecution().getExecutionContext();
                System.err.println("key-step1-step = " + stepEc.get("key-step1-step"));
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").tasklet(tasklet1()).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2").tasklet(tasklet2()).build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("execution-context-job")
                .start(step1()).next(step2())
                .incrementer(new RunIdIncrementer())
                .build();
    }


    public static void main(String[] args) {
        SpringApplication.run(ExecutionContextJob.class, args);
    }
}
