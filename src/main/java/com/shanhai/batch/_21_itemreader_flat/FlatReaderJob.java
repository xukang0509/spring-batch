package com.shanhai.batch._21_itemreader_flat;

import com.shanhai.batch.User;
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
public class FlatReaderJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public ItemWriter<User> userItemWriter() {
        return items -> items.forEach(System.err::println);
    }

    @Bean
    public FlatFileItemReader<User> userItemReader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("userItemReaderByFlat")
                // 从哪个文件读取
                .resource(new ClassPathResource("user.txt"))
                // 拆分数据，以#号分割，默认,
                .delimited().delimiter("#")
                // 拆分出来的数据 分别进行命名，后续跟对象中数据进行映射
                .names("id", "name", "age")
                // 映射到哪个对象
                .targetType(User.class)
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<User,User>chunk(1)
                .reader(userItemReader())
                .writer(userItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("flat-reader-job").start(step1()).build();
    }

    public static void main(String[] args) {
        SpringApplication.run(FlatReaderJob.class, args);
    }
}
