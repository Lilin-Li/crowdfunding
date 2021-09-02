package inc.lilin.crowd.redis;

import inc.lilin.crowd.entity.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    ResultVO<String> setRedisKeyValueRemote(
            String key,
            String value) {
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            return ResultVO.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.failed(e.getMessage());

        }

    }

    void setRedisKeyValueRemoteWithTimeout(String key,
                                           String value,
                                           long time,
                                           TimeUnit timeUnit) throws Exception{

        ValueOperations<String, String> operations = redisTemplate.opsForValue();
        operations.set(key, value, time, timeUnit);
    }

    String getRedisStringValueByKeyRemote(String key) throws Exception {
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String value = operations.get(key);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    ResultVO<String> removeRedisKeyRemote(String key) {
        try {
            redisTemplate.delete(key);
            return ResultVO.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultVO.failed(e.getMessage());
        }
    }
}