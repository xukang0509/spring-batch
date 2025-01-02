package com.shanhai.batch._07_step_simple;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
public class SimpleTaskletJob {
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
                System.out.println("------>" + System.currentTimeMillis());
                //return RepeatStatus.CONTINUABLE; // 循环执行
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").tasklet(tasklet()).build();
    }

    // 定义作业
    @Bean
    public Job job() {
        return jobBuilderFactory.get("step-simple-tasklet-job1").start(step1()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SimpleTaskletJob.class, args);
    }
}
