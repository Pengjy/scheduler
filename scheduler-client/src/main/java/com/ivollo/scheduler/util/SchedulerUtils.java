package com.ivollo.scheduler.util;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Created by Administrator on 2017/6/23.
 */
public class SchedulerUtils {

    private static final SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    public static Scheduler loadScheduler(){
        try {
            return schedulerFactory.getScheduler();
        } catch (SchedulerException e) {
            return null;
        }
    }

}
