package redistest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class RedisCommandTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
    public void testString(){
        //操作String类型的数据
        ValueOperations<String, String> valueStr = redisTemplate.opsForValue();
        //存储一条数据
        valueStr.set("goodsProdu","长安");
        //获取一条数据并输出
        String goodsName = valueStr.get("goodsProdu");
        System.out.println(goodsName);
        //存储多条数据
        Map<String,String> map = new HashMap<String, String>();
        map.put("goodsName","福特汽车");
        map.put("goodsPrice","88888");
        map.put("goodsId","88");
  
        valueStr.multiSet(map);
        //获取多条数据
        System.out.println("========================================");
        List<String>list = new ArrayList<String>();
        list.add("goodsName");
        list.add("goodsPrice");
        list.add("goodsId");
        list.add("goodsProdu");
  
        List<String> listKeys = valueStr.multiGet(list);
        for (String key : listKeys) {
            System.out.println(key);
        }
  
  
    }
	
	@Test
	public void testHash(){
		//创建对象
		HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
		//存储一条数据
		opsForHash.put("orderInfo","orderId","11");
		//获取一条数据
		String value = opsForHash.get("orderInfo", "orderId");
		System.out.println(value);
 
		//存储多条数据
		Map<String,String> map = new HashMap();
		map.put("createTime","2018-06-21");
		map.put("orderSn","888888");
		opsForHash.putAll("orderInfo",map);
		//获取多条数据
		List<String> listKey = new ArrayList();
		listKey.add("createTime");
		listKey.add("orderSn");
		List<String> info = opsForHash.multiGet("orderInfo", listKey);
		for (String s : info) {
			System.out.println(s);
 
		}
 
	}
}
