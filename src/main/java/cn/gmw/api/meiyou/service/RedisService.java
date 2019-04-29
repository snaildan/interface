package cn.gmw.api.meiyou.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public Boolean expire(final String key, final long seconds) {
        return stringRedisTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }


    public String get(Object o) {
        return stringRedisTemplate.opsForValue().get(o);
    }

    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    public void multi() {
        stringRedisTemplate.multi();
    }

    public List<Object> exec() {
        return stringRedisTemplate.exec();
    }

    public Boolean del(String key) {
        return stringRedisTemplate.delete(key);
    }

}
