package com.shanhai.batch._11_step_condition_decider;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author xk
 * @since 2024-12-11 17:23
 */
@SpringBootApplication
@EnableBatchProcessing
public class CustomizeStatusStepJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet firstTasklet() {
        return (stepContribution, chunkContext) -> {
            System.out.println("firstTasklet------>" + System.currentTimeMillis());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet taskletA() {
        return (stepContribution, chunkContext) -> {
            System.out.println("taskletA------>" + System.currentTimeMillis());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet taskletB() {
        return (stepContribution, chunkContext) -> {
            System.out.println("taskletB------>" + System.currentTimeMillis());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet taskletDefault() {
        return (stepContribution, chunkContext) -> {
            System.out.println("taskletDefault------>" + System.currentTimeMillis());
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step firstStep() {
        return stepBuilderFactory.get("firstStep")
                .tasklet(firstTasklet())
                .build();
    }

    @Bean
    public Step stepA() {
        return stepBuilderFactory.get("stepA")
                .tasklet(taskletA())
                .build();
    }

    @Bean
    public Step stepB() {
        return stepBuilderFactory.get("stepB")
                .tasklet(taskletB())
                .build();
    }

    @Bean
    public Step stepDefault() {
        return stepBuilderFactory.get("stepDefault")
                .tasklet(taskletDefault())
                .build();
    }

    // 决策器
    @Bean
    public MyStatusDecider myStatusDecider() {
        return new MyStatusDecider();
    }

    // 定义作业
    @Bean
    public Job job() {
        return jobBuilderFactory.get("condition-multi-job")
                .start(firstStep())
                .next(myStatusDecider())
                .from(myStatusDecider()).on("A").to(stepA())
                .from(myStatusDecider()).on("B").to(stepB())
                .from(myStatusDecider()).on("*").to(stepDefault())
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(CustomizeStatusStepJob.class, args);
    }
}
