package com.shanhai.batch._08_step_chunk_tasklet;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xk
 * @since 2024-12-12 16:48
 */
@SpringBootApplication
@EnableBatchProcessing
public class ChunkTaskletJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    private int timer = 10;

    @Bean
    public ItemReader<String> itemReader() {
        return new ItemReader<String>() {
            @Override
            public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                if (timer > 0) {
                    System.out.println("-------------read------------");
                    return "read-ret-" + timer--;
                }
                return null;
            }
        };
    }

    @Bean
    public ItemProcessor<String, String> itemProcessor() {
        return new ItemProcessor<String, String>() {
            @Override
            public String process(String item) throws Exception {
                System.out.println("-------------process------------" + item);
                return "process-ret->" + item;
            }
        };
    }

    @Bean
    public ItemWriter<String> itemWriter() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> items) throws Exception {
                System.out.println(items);
            }
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                //设置块的size为3次
                .<String, String>chunk(3)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("step-chunk-tasklet-job").start(step1())
                .incrementer(new RunIdIncrementer()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ChunkTaskletJob.class, args);
    }
}
