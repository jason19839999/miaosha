package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.redis.JsonUtils;
import com.imooc.miaosha.redis.MiaoshaUserKey;
import com.imooc.miaosha.redis.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("redis")
public class RedisController {

	@Autowired
	RedisService redisService;

	/**
	 *支持List转换成string,   string转换为List...
	 * @return
	 */

	@RequestMapping("/getJsonList")
	public List<User> getJsonList() {

		User user = new User();
		user.setName("慕课网");

		User u1 = new User();
		u1.setName("imooc");

		User u2 = new User();
		u2.setName("hello imooc");

		List<User> userList = new ArrayList<User>();
		userList.add(user);
		userList.add(u1);
		userList.add(u2);

		//List转string
		redisService.set(MiaoshaUserKey.token,"json:info:userlist", JsonUtils.objectToJson(userList));
		
		String userListJson = redisService.get(MiaoshaUserKey.token,"json:info:userlist");

		//string转List
		List<User> userListBorn = JsonUtils.jsonToList(userListJson, User.class);
		
		return userListBorn;
	}
}