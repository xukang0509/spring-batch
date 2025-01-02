package com.shanhai.batch._17_job_stop_sign;

import com.shanhai.batch._16_job_stop.ResouceCount1;
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
public class SignJobStopJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;
    //模拟只读取50个
    private int readCount = 100;

    @Bean
    public Tasklet tasklet1() {
        return (contribution, chunkContext) -> {
            for (int i = 1; i <= readCount; i++) {
                System.out.println("---------------step1执行-" + i + "------------------");
                ResouceCount1.readCount++;
            }
            if (ResouceCount1.readCount != ResouceCount1.totalCount) {
                chunkContext.getStepContext().getStepExecution().setTerminateOnly();
            }
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet tasklet2() {
        return (contribution, chunkContext) -> {
            System.err.println("step2执行了.......");
            System.err.println("readCount:" + ResouceCount1.readCount + ", totalCount:" + ResouceCount1.totalCount);
            return RepeatStatus.FINISHED;
        };
    }


    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(tasklet1())
                .allowStartIfComplete(true)
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
        return jobBuilderFactory.get("job-stop-job-sign")
                .start(step1())
                .next(step2())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SignJobStopJob.class, args);
    }
}