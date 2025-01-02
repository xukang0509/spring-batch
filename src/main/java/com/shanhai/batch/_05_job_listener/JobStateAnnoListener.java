package com.shanhai.batch._05_job_listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

/**
 * @author xk
 * @since 2024-12-12 10:54
 */
public class JobStateAnnoListener {

    // 作业执行前
    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        System.err.println("执行前--anno--status: " + jobExecution.getStatus());
    }

    // 作业执行后
    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        System.err.println("执行后--anno--status: " + jobExecution.getStatus());
    }
}
