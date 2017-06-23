package com.ivollo.scheduler.manage;

import org.quartz.*;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

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

    /**
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
     *
     * @param sched
     *            调度器
     *
     * @param jobName
     *            任务名
     * @param cls
     *            任务
     * @param time
     *            时间设置，参考quartz说明文档
     *
     * @Title: QuartzManager.java
     */
    public  void addJob(Scheduler sched, String jobName, @SuppressWarnings("rawtypes") Class cls, String time) {
        try {
            JobDetailImpl jobDetail = new JobDetailImpl();// 任务名，任务组，任务执行类
            jobDetail.setName(jobName);
            jobDetail.setGroup(JOB_GROUP_NAME);
            jobDetail.setJobClass(cls);
            // 触发器
            CronTriggerImpl trigger = new CronTriggerImpl();// 触发器名,触发器组
            trigger.setName(jobName);
            trigger.setGroup(TRIGGER_GROUP_NAME);
            trigger.setCronExpression(time);// 触发器时间设定
            sched.scheduleJob(jobDetail, trigger);
            // 启动
            if (!sched.isShutdown()) {
                sched.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 添加一个定时任务
     *
     * @param sched
     *            调度器
     *
     * @param jobName
     *            任务名
     * @param jobGroupName
     *            任务组名
     * @param triggerName
     *            触发器名
     * @param triggerGroupName
     *            触发器组名
     * @param jobClass
     *            任务
     * @param time
     *            时间设置，参考quartz说明文档
     *
     * @Title: QuartzManager.java
     */
    public  void addJob(Scheduler sched, String jobName, String jobGroupName, String triggerName, String triggerGroupName, @SuppressWarnings("rawtypes") Class jobClass, String time) {
        try {
            JobDetailImpl jobDetail = new JobDetailImpl();// 任务名，任务组，任务执行类
            jobDetail.setName(jobName);
            jobDetail.setGroup(jobGroupName);
            jobDetail.setJobClass(jobClass);
            // 触发器
            CronTriggerImpl trigger = new CronTriggerImpl();// 触发器名,触发器组
            trigger.setName(triggerName);
            trigger.setGroup(triggerGroupName);
            trigger.setCronExpression(time);// 触发器时间设定
            sched.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param sched
     *            调度器
     * @param jobName
     * @param time
     *
     * @Title: QuartzManager.java
     */
    @SuppressWarnings("rawtypes")
    public  void modifyJobTime(Scheduler sched, String jobName, String time) {
        try {
            CronTrigger trigger = (CronTrigger) sched.getTrigger(new TriggerKey(jobName, TRIGGER_GROUP_NAME));
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                JobDetail jobDetail = sched.getJobDetail(new JobKey(jobName, JOB_GROUP_NAME));
                Class objJobClass = jobDetail.getJobClass();
                removeJob(sched, jobName);
                addJob(sched, jobName, objJobClass, time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 修改一个任务的触发时间
     *
     * @param sched
     *            调度器 *
     * @param sched
     *            调度器
     * @param triggerName
     * @param triggerGroupName
     * @param time
     *
     * @Title: QuartzManager.java
     */
    public void modifyJobTime(Scheduler sched, String triggerName, String triggerGroupName, String time) {
        try {
            CronTrigger trigger = (CronTrigger) sched.getTrigger(new TriggerKey(triggerName, triggerGroupName));
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
                CronTriggerImpl ct = (CronTriggerImpl) trigger;
                // 修改时间
                ct.setCronExpression(time);
                // 重启触发器
                sched.resumeTrigger(new TriggerKey(triggerName, triggerGroupName));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
     *
     * @param sched
     *            调度器
     * @param jobName
     *
     * @Title: QuartzManager.java
     */
    public  void removeJob(Scheduler sched, String jobName) {
        removeJob(sched,jobName,JOB_GROUP_NAME,jobName,TRIGGER_GROUP_NAME);
    }

    /**
     * @Description: 移除一个任务
     *
     * @param sched
     *            调度器
     * @param jobName
     * @param jobGroupName
     * @param triggerName
     * @param triggerGroupName
     *
     * @Title: QuartzManager.java
     */
    public  void removeJob(Scheduler sched, String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
        try {
            sched.pauseTrigger(new TriggerKey(triggerName, triggerGroupName));// 停止触发器
            sched.unscheduleJob(new TriggerKey(triggerName, triggerGroupName));// 移除触发器
            sched.deleteJob(new JobKey(jobName, jobGroupName));// 删除任务
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:启动所有定时任务
     *
     * @param sched
     *            调度器
     *
     * @Title: QuartzManager.java
     */
    public  void startJobs(Scheduler sched) {
        try {
            sched.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @Description:关闭所有定时任务
     *
     *
     * @param sched
     *            调度器
     *
     *
     * @Title: QuartzManager.java
     */
    public void shutdownJobs(Scheduler sched) {
        try {
            if (!sched.isShutdown()) {
                sched.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}