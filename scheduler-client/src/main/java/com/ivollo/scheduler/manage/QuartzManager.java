package com.ivollo.scheduler.manage;

import com.ivollo.scheduler.bean.DetailQuartzBean;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Quartz调度管理器
 *
 * @author Administrator
 *
 */
@Component
public class QuartzManager {
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private RedisTemplate redisTemplate;

    private HashMap<String, JobListener> internalJobListeners = new HashMap<String, JobListener>(10);

    public  void addJob(String jobName,String bean, String time) {
        try {

            Map<String, Object> jobDataAsMap = new HashMap<String, Object>();
            jobDataAsMap.put("targetObject",bean);
            jobDataAsMap.put("targetMethod","execute");

            JobDetailImpl jdi = new JobDetailImpl();
            jdi.setName(jobName);
            jdi.setJobClass(DetailQuartzBean.class);
            jdi.getJobDataMap().putAll(jobDataAsMap);

            CronTriggerFactoryBean cronTriggerBean = new CronTriggerFactoryBean();
            cronTriggerBean.setJobDetail(jdi);
            cronTriggerBean.setCronExpression(time);
            cronTriggerBean.setName(jobName+"Trigger");
            cronTriggerBean.afterPropertiesSet();

            System.out.println("===============in=================");
            while(getEntryLock("lock:job:"+jobName)){
                Thread.sleep(100);
            }
            boolean  isExist = schedulerFactoryBean.getScheduler().getJobDetail(new JobKey(jobName))!=null;

            if(!isExist) {
                schedulerFactoryBean.getScheduler().scheduleJob(jdi, cronTriggerBean.getObject());
            }else{
                schedulerFactoryBean.getScheduler().rescheduleJob(cronTriggerBean.getObject().getKey(), cronTriggerBean.getObject());
            }

            schedulerFactoryBean.getScheduler().start();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            releaseEntryLock("lock:job:"+jobName);
        }
    }

    public void delJob(String jobName){
        try {
            schedulerFactoryBean.getScheduler().unscheduleJob(new TriggerKey(jobName+"Trigger"));
            schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobName));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }


    private  Boolean getEntryLock(final String key){
        if (key == null) {
            return false;
        }

       Boolean res = (Boolean) redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                try {
                    redisConnection.openPipeline();
                    redisConnection.incr(key.getBytes());
                    redisConnection.expire(key.getBytes(), 30);
                    List<Object> result = redisConnection.closePipeline();
                    if ((Long) result.get(0) == 1l) {
                        return true;
                    } else {
                        return false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        return res;
    }

    private void releaseEntryLock(final String key){
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.del(key.getBytes());
                return null;
            }
        });
    }
}
