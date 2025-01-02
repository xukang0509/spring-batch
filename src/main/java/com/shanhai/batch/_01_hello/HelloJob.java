package com.shanhai.batch._01_hello;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
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
public class HelloJob {
    // job调度器
    @Resource
    private JobLauncher jobLauncher;
    // job构造器工厂
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    // step构造器工厂
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    // 任务--step执行逻辑由tasklet完成
    @Bean
    public Tasklet tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("Hello SpringBatch...");
                return RepeatStatus.FINISHED;
            }
        };
    }

    // 作业步骤--不带读/写/处理
    @Bean
    public Step step() {
        return stepBuilderFactory.get("step1").tasklet(tasklet()).build();
    }

    // 定义作业
    @Bean
    public Job job() {
        return jobBuilderFactory.get("hello-job1").start(step()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloJob.class, args);
    }
}
