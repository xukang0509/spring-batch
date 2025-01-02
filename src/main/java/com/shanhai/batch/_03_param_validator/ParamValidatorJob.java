package com.shanhai.batch._03_param_validator;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;

/**
 * @author xk
 * @since 2024-12-12 9:50
 */
@SpringBootApplication
@EnableBatchProcessing
public class ParamValidatorJob {
    @Resource
    private JobBuilderFactory jobBuilderFactory;
    @Resource
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Tasklet tasklet() {
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                Map<String, Object> parameters = chunkContext.getStepContext().getJobParameters();
                System.out.println("params--name--valid:" + parameters.get("name"));
                System.out.println("params--age--valid:" + parameters.get("age"));
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1").tasklet(tasklet()).build();
    }

    // 配置name参数校验器
    @Bean
    public NameParamValidator nameParamValidator() {
        return new NameParamValidator();
    }

    // 配置default参数校验器
    @Bean
    public DefaultJobParametersValidator defaultJobParametersValidator() {
        DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator();
        defaultJobParametersValidator.setRequiredKeys(new String[]{"name"}); // 必填
        defaultJobParametersValidator.setOptionalKeys(new String[]{"age"});  // 可选
        return defaultJobParametersValidator;
    }

    // 配置组合参数校验器
    @Bean
    public CompositeJobParametersValidator compositeJobParametersValidator() {
        CompositeJobParametersValidator compositeJobParametersValidator = new CompositeJobParametersValidator();
        // 按照传入的顺序，先执行默认校验器，再执行名称校验器
        compositeJobParametersValidator.setValidators(Arrays.asList(defaultJobParametersValidator(), nameParamValidator()));
        try {
            // 判断校验器是否为空
            compositeJobParametersValidator.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compositeJobParametersValidator;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("name-param-validator-job")
                .start(step1())
                //.validator(nameParamValidator()) // 配置名称参数校验器
                //.validator(defaultJobParametersValidator()) // 配置default参数校验器
                .validator(compositeJobParametersValidator()) // 配置组合参数校验器
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(ParamValidatorJob.class, args);
    }
}
