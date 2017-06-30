package com.ivollo.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * Created by Administrator on 2017/6/23.
 */
@Component("myJob")
public class MyJob extends QuartzJobBean{

    private static final long serialVersionUID = 3698597735644612655L;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        System.out.println("xxxxxx" + Thread.currentThread().getId());
    }

    @PreDestroy
    public void destroy() throws Exception {
        System.out.println("========================dfd=======");
    }
}
