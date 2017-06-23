package com.ivollo.scheduler.client;

import com.google.protobuf.InvalidProtocolBufferException;
import com.ivollo.scheduler.config.ConfigProto;
import com.ivollo.scheduler.constant.Operations;
import com.ivollo.scheduler.manage.QuartzManager;
import com.ivollo.scheduler.util.SchedulerUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/6/22.
 */
@Component
public class ConfigSubListener implements MessageListener ,ApplicationContextAware{

    @Autowired
    private QuartzManager quartzManager;

    private ApplicationContext applicationContext;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        try {
            ConfigProto.config config = ConfigProto.config.parseFrom(message.getBody());
            if(Operations.ADD.equals(config.getOpreation())){
                quartzManager.addJob(SchedulerUtils.loadScheduler(), config.getJobName(),findBeanClass(config.getBeanName()),config.getCron());
            }else if(Operations.DEL.equals(config.getOpreation())){
                quartzManager.removeJob(SchedulerUtils.loadScheduler(),config.getJobName());
            }else if(Operations.MODIFY.equals(config.getOpreation())){
                quartzManager.modifyJobTime(SchedulerUtils.loadScheduler(),config.getJobName(),config.getCron());
            }
        } catch (InvalidProtocolBufferException e) {
        }
    }

    private Class findBeanClass(String beanName){
        return applicationContext.getBean(beanName).getClass();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
