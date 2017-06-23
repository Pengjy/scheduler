package com.ivollo.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/6/23.
 */
@Component("myJob")
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("xxxxxx" + Thread.currentThread().getId());
    }
}
