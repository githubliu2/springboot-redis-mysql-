package com.spring.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.entity.User;
import com.spring.util.RedisUtil;

import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/redis/cache")
public class RedisDealCacheQuestion {
	 @Autowired
     private StringRedisTemplate stringRedisTemplate;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private JdbcTemplate jdbcTemplate; 
	// 缓存穿透
	/*
	 * 大量请求过来查询 缓存和数据库中都没有数据，请求过来查询结果为null，这样就造成数据库短时间线程数被打满，
	 * 导致其他服务被阻塞，从而导致线上服务不可用）
	 */
    @GetMapping(value = "penetrateCache")
    public String penetrateCache(String key) {
    	Jedis jedis = redisUtil.initRedis(0);
    	
    	String cacheValue = jedis.get(key);
    	
    	// 查数据库
    	List<User> users = new ArrayList<User>();
    	if (cacheValue == null) {
       	 String sql = "select uid, username, password from user";
    	 
//       	 users = jdbcTemplate.query(sql, new RowMapper<User>() {
//                public User mapRow(ResultSet resultSet, int i) throws SQLException {
//                    User user = new User();
//                    user.setUid(new Integer(resultSet.getString("uid")));
//                    user.setUserName(resultSet.getString("userName"));
//                    user.setPassword(resultSet.getString("password"));
//
//                    return user;
//                }
//            });
//            System.out.println("查询成功："  +  users);
    	}
    	
		return cacheValue == null && users.isEmpty() ? "缓存穿透: 缓存和数据库都没有数据"
				: cacheValue != null ? "命中缓存" : !users.isEmpty() ? "查询数据库" : "查询数据库";
		/*
		 *  方案一：缓存空对象
			方案二：布隆过滤器拦截
		 */
    }
    
	// 缓存击穿 breakdown n.
    /*
     * 缓存中没有数据，数据库中存在数据，一般都是大量热点数据过期，
     * 这时由于大量请求过来查询，同时去读缓存中没有数据，然后就去数据库查，
     * 大量请求导致数据库压力剧增，线上服务就卡住了）
     */
    @GetMapping(value = "shatterCache")
    public String shatterCache(String key) {
    	Jedis jedis = redisUtil.initRedis(0);
    	
    	String cacheValue = jedis.get(key);
    	
    	// 查数据库
    	List<User> users = new ArrayList<User>();
    	if (cacheValue == null) {
          	 String sql = "select uid, username, password from user";
        	 
           	 users = jdbcTemplate.query(sql, new RowMapper<User>() {
                    public User mapRow(ResultSet resultSet, int i) throws SQLException {
                        User user = new User();
                        user.setUid(new Integer(resultSet.getString("uid")));
                        user.setUserName(resultSet.getString("userName"));
                        user.setPassword(resultSet.getString("password"));

                        return user;
                    }
                });
                System.out.println("查询成功："  +  users);
    	}
    	
		return cacheValue == null && users.isEmpty() ? "缓存穿透: 缓存和数据库都没有数据"
				: cacheValue != null ? "命中缓存" : !users.isEmpty() ? "没命中缓存 查询数据库请求太多 造成缓存击穿" : "查询数据库";
		/*
		 *  方案一：在数据库中维护一张热点数据表，设置时间为永不过期，并定时更新数据库中的数据
			方案二：加互斥锁（互斥锁就是当此线程空闲的时候才能进行查询，当线程获取不到的时候，我们就必须等待）
		 */
    }
	// 缓存雪崩
    /**
     * 用一时间  大量key失效  请求全部转发到DB，DB瞬时压力过重雪崩。
     */
    //每个Key的失效时间都加个随机值
    //设置热点数据永远不过期，有更新操作就更新缓存
    // 加互斥锁。该方式和缓存击穿一样，按 key 维度加锁，对于同一个 key，只允许一个线程去计算，其他线程原地阻塞等待第一个线程的计算结果，然后直接走缓存即可。
}
