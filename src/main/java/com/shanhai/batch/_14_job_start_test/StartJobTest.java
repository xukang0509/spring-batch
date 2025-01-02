package com.shanhai.batch._14_job_start_test;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author xk
 * @since 2024-12-12 19:11
 */
@SpringBootTest(classes = App.class)
public class StartJobTest {
    @Resource
    private JobLauncher jobLauncher;
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    public Tasklet tasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Hello SpringBatch....");
            return RepeatStatus.FINISHED;
        };
    }

    public Step step() {
        return stepBuilderFactory.get("step").tasklet(tasklet()).build();
    }

    public Job job() {
        return jobBuilderFactory.get("start-test-job").start(step())
                .incrementer(new RunIdIncrementer()).build();
    }

    @Test
    public void testStart() throws Exception {
        // job作业启动
        // 参数一：作业实例；参数二：作业运行携带参数
        this.jobLauncher.run(job(), new JobParameters());
    }
}
