package com.shanhai.batch._18_job_restart_forbid;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;

@SpringBootApplication
@EnableBatchProcessing
public class JobForBidRestartJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet1() {
        return (contribution, chunkContext) -> {
            System.err.println("-------------tasklet1-------------");
            //chunkContext.getStepContext().getStepExecution().setTerminateOnly(); //停止步骤
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet tasklet2() {
        return (contribution, chunkContext) -> {
            System.err.println("-------------tasklet2-------------");
            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(tasklet1())
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(tasklet2())
                .build();
    }

    //定义作业
    @Bean
    public Job job() {
        return jobBuilderFactory.get("job-forbid-restart-job")
                .preventRestart()  //禁止重启
                .start(step1())
                .next(step2())
                .build();

    }

    public static void main(String[] args) {
        SpringApplication.run(JobForBidRestartJob.class, args);
    }
}