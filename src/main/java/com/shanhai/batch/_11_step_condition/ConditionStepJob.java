package com.shanhai.batch._11_step_condition;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
public class ConditionStepJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet firstTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("firstTasklet------>" + System.currentTimeMillis());
                //return RepeatStatus.FINISHED;
                throw new RuntimeException("测试fail结果");
            }
        };
    }

    @Bean
    public Tasklet successTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("successTasklet------>" + System.currentTimeMillis());
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Tasklet failTasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext)
                    throws Exception {
                System.out.println("failTasklet------>" + System.currentTimeMillis());
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step firstStep() {
        return stepBuilderFactory.get("firstStep")
                .tasklet(firstTasklet())
                .build();
    }

    @Bean
    public Step successStep() {
        return stepBuilderFactory.get("successStep")
                .tasklet(successTasklet())
                .build();
    }

    @Bean
    public Step failStep() {
        return stepBuilderFactory.get("failStep")
                .tasklet(failTasklet())
                .build();
    }

    // 定义作业
    @Bean
    public Job job() {
        return jobBuilderFactory.get("condition-multi-job")
                .start(firstStep())
                .on(ExitStatus.FAILED.getExitCode()).to(failStep())
                .from(firstStep()).on("*").to(successStep())
                .end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ConditionStepJob.class, args);
    }
}
