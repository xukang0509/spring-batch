package com.shanhai.batch._09_step_listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * @author xk
 * @since 2024-12-12 18:01
 */
public class MyStepListener implements StepExecutionListener {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("-----------beforeStep--------->");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("-----------afterStep--------->");
        // 不改动返回状态
        return stepExecution.getExitStatus();
    }
}
