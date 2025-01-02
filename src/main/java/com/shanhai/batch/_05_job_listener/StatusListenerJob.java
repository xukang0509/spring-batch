package com.shanhai.batch._05_job_listener;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
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
public class StatusListenerJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("执行中--anno--status: " + contribution.getStepExecution().getJobExecution().getStatus());
                return RepeatStatus.FINISHED;
            }
        };
    }

    // 状态监听器
    @Bean
    public JobStateListener jobStateListener() {
        return new JobStateListener();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step1").tasklet(tasklet()).build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("status-anno-listener-job")
                .start(step())
                .incrementer(new RunIdIncrementer())
                //.listener(jobStateListener()) //设置状态监听器
                .listener(JobListenerFactoryBean.getListener(new JobStateAnnoListener()))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(StatusListenerJob.class, args);
    }
}
