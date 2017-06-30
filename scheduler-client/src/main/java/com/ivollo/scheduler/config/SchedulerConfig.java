package com.ivollo.scheduler.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Administrator on 2017/6/28.
 */
@Configuration
public class SchedulerConfig {

    @Bean(destroyMethod = "destroy")
    public SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setDataSource(druidDataSource());
        Resource configLocation = new ClassPathResource("scheduler_quartz.properties");
        schedulerFactoryBean.setConfigLocation(configLocation);
        schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
        return schedulerFactoryBean;
    }

    private DruidDataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        Properties properties = config();
        druidDataSource.setUrl(String.valueOf(properties.get("url")));
        druidDataSource.setUsername(String.valueOf(properties.get("username")));
        druidDataSource.setPassword(String.valueOf(properties.get("password")));
        druidDataSource.setInitialSize(Integer.parseInt(properties.getProperty("initialSize","3")));
        druidDataSource.setMaxActive(Integer.parseInt(properties.getProperty("maxActive","20")));
        druidDataSource.setMinIdle(Integer.parseInt(properties.getProperty("minIdle","3")));
        druidDataSource.setMaxWait(Long.parseLong(properties.getProperty("maxWait","10000")));
        druidDataSource.setValidationQuery(String.valueOf(properties.getProperty("validationQuery","SELECT 1")));
        return  druidDataSource;
    }

    private Properties config(){
        String propFileName = "damon_scheduler.properties";
        Properties props = new Properties();
        InputStream in = null;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(in);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return props;
    }
}
