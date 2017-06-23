package com.ivollo.scheduler.service;

import com.ivollo.scheduler.config.ConfigProto;
import com.ivollo.scheduler.constant.Channel;
import com.ivollo.scheduler.vo.ConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 配置服务
 * Created by Administrator on 2017/6/22.
 */
@Service
public class ConfigurationService {

    @Autowired
    private RedisTemplate redisTemplate;

    public void config(ConfigVO configVO){
        ConfigProto.config.Builder config = ConfigProto.config.newBuilder();
        config.setOpreation(configVO.getOperation());
        config.setJobName(configVO.getJobName());
        if(!StringUtils.isEmpty(configVO.getBeanName())) {
            config.setBeanName(configVO.getBeanName());
        }else{
            config.setBeanName("");
        }
        if(!StringUtils.isEmpty(configVO.getCron())) {
            config.setCron(configVO.getCron());
        }else{
            config.setCron("");
        }
        final byte[] content = config.build().toByteArray();
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.publish(Channel.DEFAULT_CHANNEL.getBytes(),content);
                return null;
            }
        });
    }
}
