package com.ivollo.scheduler.run;

import com.ivollo.scheduler.client.ConfigSubListener;
import com.ivollo.scheduler.constant.Channel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;

/**
 * Created by Administrator on 2017/6/23.
 */
@Component
public class InitSchedulerApplication implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ConfigSubListener configSubListener;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void initialize() {
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    redisConnection.subscribe(configSubListener, Channel.DEFAULT_CHANNEL.getBytes());
                    return null;
                }
            });
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
        } finally {
        }
    }

}
