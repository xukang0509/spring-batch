package com.shanhai.batch._02_params;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
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
public class ParamJob {

    // job构造器工厂
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    // step构造器工厂
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    // 任务--step执行逻辑由tasklet完成
    @Bean
    @StepScope
    /*public Tasklet tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
                    throws Exception {
                Map<String, Object> parameters = chunkContext.getStepContext().getJobParameters();
                System.out.println("Param--name:" + parameters.get("name"));
                return RepeatStatus.FINISHED;
            }
        };
    }*/
    public Tasklet tasklet(@Value("#{jobParameters['name']}") String name) {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("Params--name:" + name);
                return RepeatStatus.FINISHED;
            }
        };
    }

    // 作业步骤--不带读/写/处理
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").tasklet(tasklet(null)).build();
    }

    // 定义作业
    @Bean
    /*public Job job() {
        return jobBuilderFactory.get("param-job").start(step1()).build();
    }*/
    /*public Job job() {
        return jobBuilderFactory.get("param-chunk-job").start(step1()).build();
    }*/
    public Job job() {
        return jobBuilderFactory.get("param-value-job").start(step1()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ParamJob.class, args);
    }
}
