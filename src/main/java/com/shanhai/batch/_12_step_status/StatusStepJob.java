package com.shanhai.batch._12_step_status;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
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
public class StatusStepJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet firstTasklet() {
        return (stepContribution, chunkContext) -> {
            System.out.println("----------------firstTasklet------------------");
            throw new RuntimeException("假装失败了");
            //return RepeatStatus.FINISHED;  //执行完了
        };
    }

    @Bean
    public Tasklet successTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("----------------successTasklet--------------");
            return RepeatStatus.FINISHED;  //执行完了
        };
    }

    @Bean
    public Tasklet failTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("----------------failTasklet---------------");
            return RepeatStatus.FINISHED;  //执行完了
        };
    }

    @Bean
    public Step firstStep(){
        return stepBuilderFactory.get("firstStep")
                .tasklet(firstTasklet())
                .build();
    }

    @Bean
    public Step successStep(){
        return stepBuilderFactory.get("successStep")
                .tasklet(successTasklet())
                .build();
    }

    @Bean
    public Step failStep(){
        return stepBuilderFactory.get("failStep")
                .tasklet(failTasklet())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("status-step-job")
                .start(firstStep())
                // 表示当前本应该是失败结束的步骤直接转成正常结束---COMPLETED
                //.on(ExitStatus.FAILED.getExitCode()).end()
                // 表示当前本应该是失败结束的步骤直接转成失败结束---FAILED
                //.on(ExitStatus.FAILED.getExitCode()).fail()
                // 表示当前本应该是失败结束的步骤直接转成停止结束---STOPPED 里面参数表示后续要重启时，从successStep位置开始
                //.on(ExitStatus.FAILED.getExitCode()).stopAndRestart(successStep())
                //.from(firstStep()).on("*").to(successStep())
                //.end()
                .incrementer(new RunIdIncrementer())
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(StatusStepJob.class, args);
    }
}
