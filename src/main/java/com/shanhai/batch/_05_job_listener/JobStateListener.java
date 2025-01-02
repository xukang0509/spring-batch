package com.shanhai.batch._05_job_listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * @author xk
 * @since 2024-12-12 10:43
 */
public class JobStateListener implements JobExecutionListener {

    // 作业执行前
    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.err.println("执行前--status: " + jobExecution.getStatus());
    }

    // 作业执行后
    @Override
    public void afterJob(JobExecution jobExecution) {
        System.err.println("执行后--status: " + jobExecution.getStatus());
    }
}
