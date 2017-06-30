package com.ivollo.scheduler.run;

import com.ivollo.scheduler.bean.DetailQuartzBean;
import com.ivollo.scheduler.client.ConfigSubListener;
import com.ivollo.scheduler.constant.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
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
public class InitSchedulerApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitSchedulerApplication.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ConfigSubListener configSubListener;

    @PostConstruct
    public void initialize() {
        SubscribeRunner subscribeRunner = new SubscribeRunner( redisTemplate, configSubListener);
        Thread thread = new Thread(subscribeRunner);
        thread.start();
    }
}

class SubscribeRunner implements Runnable{

    private RedisTemplate redisTemplate;

    private ConfigSubListener configSubListener;

    public SubscribeRunner(RedisTemplate redisTemplate,ConfigSubListener configSubListener){
        this.redisTemplate = redisTemplate;
        this.configSubListener = configSubListener;
    }
    @Override
    public void run() {
        try {
            redisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                    redisConnection.pSubscribe(configSubListener, Channel.DEFAULT_CHANNEL.getBytes());
                    return null;
                }
            });
        } catch (Exception e) {
            System.out.println(String.format("subsrcibe channel error, %s", e));
        } finally {
        }
    }
}