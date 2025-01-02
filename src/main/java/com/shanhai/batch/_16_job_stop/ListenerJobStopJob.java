package com.shanhai.batch._16_job_stop;

import org.springframework.batch.core.ExitStatus;
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

@SpringBootApplication
@EnableBatchProcessing
public class ListenerJobStopJob {
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
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet tasklet2() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.err.println("step2执行了.......");
                System.err.println("readCount:" + ResouceCount1.readCount + ", totalCount:" + ResouceCount1.totalCount);
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public StopStepListener stopStepListener() {
        return new StopStepListener();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(tasklet1())
                .listener(stopStepListener())
                .allowStartIfComplete(true)  //执行完后，运行重启
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
        return jobBuilderFactory.get("job-stop-job2")
                .start(step1())
                .on(ExitStatus.STOPPED.getExitCode()).stopAndRestart(step1())
                .from(step1()).on("*").to(step2()).end()
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ListenerJobStopJob.class, args);
    }
}