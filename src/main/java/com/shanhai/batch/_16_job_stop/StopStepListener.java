package com.shanhai.batch._16_job_stop;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * @author xk
 * @since 2024-12-12 19:44
 */
public class StopStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        // 不满足
        if (ResouceCount1.totalCount != ResouceCount1.readCount) {
            // 手动停止，后续可以重启
            return ExitStatus.STOPPED;
        }
        return stepExecution.getExitStatus();
    }
}
