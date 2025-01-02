package com.shanhai.batch._13_flow_step;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

/**
 * @author xk
 * @since 2024-12-12 18:51
 */
@SpringBootApplication
@EnableBatchProcessing
public class FlowStepJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet taskletA() {
        return (contribution, chunkContext) -> {
            System.out.println("================taskletA================");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet taskletB1() {
        return (contribution, chunkContext) -> {
            System.out.println("================taskletB1================");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet taskletB2() {
        return (contribution, chunkContext) -> {
            System.out.println("================taskletB2================");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet taskletB3() {
        return (contribution, chunkContext) -> {
            System.out.println("================taskletB3================");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet taskletC() {
        return (contribution, chunkContext) -> {
            System.out.println("================taskletC================");
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step stepA() {
        return stepBuilderFactory.get("stepA").tasklet(taskletA()).build();
    }

    @Bean
    public Step stepB1() {
        return stepBuilderFactory.get("stepB1").tasklet(taskletB1()).build();
    }

    @Bean
    public Step stepB2() {
        return stepBuilderFactory.get("stepB2").tasklet(taskletB2()).build();
    }

    @Bean
    public Step stepB3() {
        return stepBuilderFactory.get("stepB3").tasklet(taskletB3()).build();
    }

    @Bean
    public Flow flowB() {
        return new FlowBuilder<Flow>("flowB")
                .start(stepB1())
                .next(stepB2())
                .next(stepB3())
                .build();
    }

    @Bean
    public Step stepB() {
        return stepBuilderFactory.get("stepB").flow(flowB()).build();
    }

    @Bean
    public Step stepC() {
        return stepBuilderFactory.get("stepC").tasklet(taskletC()).build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("flow-step-job")
                .start(stepA())
                .next(stepB()).next(stepC())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(FlowStepJob.class, args);
    }
}
