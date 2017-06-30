package com.ivollo.scheduler.bean;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/28.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class DetailQuartzBean extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DetailQuartzBean.class);

    private ApplicationContext ctx;
    private String targetObject;
    private String targetMethod;

    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        try {
            Object otargetObject = ctx.getBean(targetObject);
            Method m = otargetObject.getClass().getMethod(targetMethod, new Class[] {JobExecutionContext.class});
            m.invoke(otargetObject, new Object[] {context});
        } catch (Exception e) {
            throw new JobExecutionException(e);
        }
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.ctx = applicationContext;
    }

    public void setTargetObject(String targetObject) {
        this.targetObject = targetObject;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }
}
