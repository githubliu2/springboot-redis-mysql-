package com.spring.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.entity.User;
import com.spring.util.RedisUtil;

import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/redis")
public class RedisClientTest {
	 @Autowired
    private StringRedisTemplate stringRedisTemplate;
	 @Autowired
	 private RedisUtil redisUtil;
	 @Autowired
	 private JdbcTemplate jdbcTemplate; 
 
    @GetMapping(value = "getUserByRedis")
    public String getIndex(){
        stringRedisTemplate.opsForValue().set("中文", "中文值");
        String res = stringRedisTemplate.opsForValue().get("xiaocai");
        System.out.println(res);
        Jedis jedis = redisUtil.initRedis(0);
        String jedisResult = jedis.get("中文");
        if (res != null) {
        	 String sql = "select uid, username, password from user";
        	 
        	 List<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
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
        return res + "---" + jedisResult;
    }

    @GetMapping(value = "insertUserAndPassword")
    public String insertUserAndPassword(String username, String password){
        Jedis jedis = redisUtil.initRedis(0);
        String jedisResult = jedis.get("中文");
        System.out.println(jedisResult);
        if (jedisResult != null) {
        	 final String sql = "insert into mysql_user(userName,password) values(?,?)";
        	 final String userName = username;
        	 final String passWord = password;
        	 jdbcTemplate.update(sql, new PreparedStatementSetter() {
        	        public void setValues(PreparedStatement preparedStatement) throws SQLException {
        	        	preparedStatement.setString(1, userName);
        	        	preparedStatement.setString(2, passWord);
        	        }
        	    });
        	 
             
        	 String querySql = "select uid, username, password from mysql_user";
        	 List<User> users = jdbcTemplate.query(querySql, new RowMapper<User>() {
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
        return jedisResult;
    }
}
