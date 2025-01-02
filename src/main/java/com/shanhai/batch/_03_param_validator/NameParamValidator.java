package com.shanhai.batch._03_param_validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

/**
 * @author xk
 * @since 2024-12-12 9:46
 */
public class NameParamValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String name = parameters.getString("name");
        if (!StringUtils.hasText(name)) {
            throw new JobParametersInvalidException("name参数不能为null或者不能为空串");
        }
    }
}
