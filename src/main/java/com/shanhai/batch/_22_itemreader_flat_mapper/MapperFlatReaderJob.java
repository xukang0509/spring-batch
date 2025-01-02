package com.shanhai.batch._22_itemreader_flat_mapper;

import com.shanhai.batch.User2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;

/**
 * @author xk
 * @since 2024-12-13 13:48
 */
@SpringBootApplication
@EnableBatchProcessing
public class MapperFlatReaderJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemWriter<User2> userItemWriter() {
        return items -> items.forEach(System.err::println);
    }

    @Bean
    public FlatFileItemReader<User2> userItemReader() {
        return new FlatFileItemReaderBuilder<User2>()
                .name("userMapperItemReader")
                .resource(new ClassPathResource("user2.txt"))
                .delimited().delimiter("#")
                .names("id", "name", "age", "province", "city", "area")
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<User2, User2>chunk(1)
                .reader(userItemReader())
                .writer(userItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("mapper-flat-reader-job").start(step1()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(MapperFlatReaderJob.class, args);
    }
}
