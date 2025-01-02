package com.shanhai.batch._04_param_incr;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.lang.Nullable;

/**
 * 时间戳作业增量器
 * @author xk
 * @since 2024-12-12 10:31
 */
public class DailyTimestampParamIncrementer implements JobParametersIncrementer {
    private static String DAILY_TIME_KEY = "daily.time";

    private String key = DAILY_TIME_KEY;

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public JobParameters getNext(@Nullable JobParameters parameters) {
        JobParameters params = parameters == null ? new JobParameters() : parameters;
        return new JobParametersBuilder(params)
                .addLong(key, System.currentTimeMillis())
                .toJobParameters();
    }
}
