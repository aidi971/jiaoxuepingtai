//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jeesite.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeesite.common.json.RedisObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisUtils {
    private static RedisTemplate<String, Object> redisTemplate = (RedisTemplate)SpringUtils.getBean("redisTemplate");

    public RedisUtils() {
    }

    public static boolean expire(String key, long time, TimeUnit timeUnit) {
        try {
            if (time > 0L) {
                redisTemplate.expire(key, time, timeUnit);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public static boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public static void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }

    }

    public static void delByPattern(String pattern) {
        Set set = keys(pattern);
        if (!set.isEmpty()) {
            redisTemplate.delete(set);
        }

    }

    public static Object get(String key) {
        return StringUtils.isNotBlank(key) ? redisTemplate.opsForValue().get(key) : null;
    }

    public static <T> T get(String key, Class<T> clazz) {
        try {
            if (StringUtils.isNotBlank(key)) {
                Object object = redisTemplate.opsForValue().get(key);
                if (object != null) {
                    Object obj = clazz.newInstance();
                    ConvertUtils.register(new DateConverter((Object)null), Date.class);
                    BeanUtils.populate(obj, (Map)object);
                    return (T)obj;
                }
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return null;
    }

    public static String getForJson(String key) {
        if (StringUtils.isNotBlank(key)) {
            Object object = redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                return objectMapper.writeValueAsString(object);
            } catch (Exception var4) {
                var4.printStackTrace();
            }
        }

        return null;
    }

    public static boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean set(String key, Object value, long time) {
        try {
            if (time > 0L) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else {
            return redisTemplate.opsForValue().increment(key, delta);
        }
    }

    public static long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        } else {
            return redisTemplate.opsForValue().increment(key, -delta);
        }
    }

    public static Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    public static Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public static boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public static boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public static void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    public static boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    public static double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    public static double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    public static Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public static long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                expire(key, time);
            }

            return count;
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0L;
        }
    }

    public static long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0L;
        }
    }

    public static long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public static List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0L;
        }
    }

    public static Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public static boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public static boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0L) {
                expire(key, time);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public static long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception var5) {
            var5.printStackTrace();
            return 0L;
        }
    }

    public static Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static Set<Object> zRevRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, start, end);
        } catch (Exception var6) {
            var6.printStackTrace();
            return null;
        }
    }

    public static long zRemRangeByRank(String key, long start, long end) {
        try {
            Long range = redisTemplate.opsForZSet().removeRange(key, start, end);
            return range;
        } catch (Exception var6) {
            var6.printStackTrace();
            return 0L;
        }
    }

    public static long zCard(String key) {
        try {
            Long count = redisTemplate.opsForZSet().zCard(key);
            return count;
        } catch (Exception var2) {
            var2.printStackTrace();
            return 0L;
        }
    }

    public static Set keys(String pattern) {
        try {
            Set set = redisTemplate.keys(pattern);
            return set;
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    static {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        RedisObjectMapper redisObjectMapper = new RedisObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(redisObjectMapper);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Object.class));
    }
}
