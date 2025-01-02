package com.shanhai.batch._15_job_start_restful;

import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;

import javax.annotation.Resource;

/**
 * @author xk
 * @since 2024-12-12 19:23
 */
//@RestController
public class HelloController {
    @Resource
    private JobLauncher jobLauncher;
    @Resource
    private Job job;
    @Resource
    private JobExplorer jobExplorer;

    // http://localhost:8080/job/start
    //@GetMapping("/job/start")
    public ExitStatus start(String name) throws Exception {
        JobParameters jobParameters = new JobParametersBuilder(jobExplorer).getNextJobParameters(job)
                .addString("name", name).toJobParameters();
        JobExecution jobExecution = this.jobLauncher.run(job, jobParameters);
        return jobExecution.getExitStatus();
    }
}
