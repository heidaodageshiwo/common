package com.common.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.common.entity.User;
import com.common.common.mapper.UserMapper;
import com.common.common.service.UserService;
import com.common.common.util.RedisUtils;
import com.sun.javafx.collections.MappingChange.Map;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.controller
 * @ClassName: UserController
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-11-18 14:05
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-11-18 14:05
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@RestController
public class UserController {

  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserService userService;

  @Autowired
  private RedisUtils redisUtils;

  //mybatisplus分支操作 redis  redission


 /* @Autowired
  private RedisTemplate redisTemplate;
*/
  @RequestMapping("/redission")
  public List<User> redission() {




    List<User> userList = userMapper.selectList(null);
    return userList;
  }

  @GetMapping("/test4")
  public void test4() {
    /*Config config = new Config();
    SingleServerConfig singleServerConfig = config.useSingleServer()
        .setAddress("192.168.56.213:6379");
    RedissonClient redisson = Redisson.create(singleServerConfig);*/
    Config config = new Config();
    config.useSingleServer().setAddress("redis://192.168.56.213:6379");
    // 2. Create Redisson instance
    RedissonClient redissonClient = Redisson.create(config);

    String stockKey = "product_1001";
    RLock lock = redissonClient.getLock(stockKey);
    try {
      // 加锁
      lock.lock();
      int stock = Integer.parseInt( redisUtils.get("stock")+"");
      if (stock > 0) {
        int realStock = stock -1;
        redisUtils.set("stock", String.valueOf(realStock));
        System.out.println("扣减成功，剩余库存：" + realStock);
      } else {
        System.out.println("扣减失败，库存不足");
      }
    } finally {
      // 释放锁
      lock.unlock();
    }
  }

  @RequestMapping("/redistest")
  public List<User> redistest() {
    boolean set = redisUtils.set("zhangqiang", "zhangqiang1");
    String zhangqiang1 = (String) redisUtils.get("zhangqiang");
    System.out.println(zhangqiang1);
    Object zhangqiang = redisUtils.get("zhangqiang");
    System.out.println(zhangqiang);
    System.out.println(set+"===============");
    System.out.println(("----- selectAll method test ------"));
    List<User> userList = userMapper.selectList(null);
//    Assert.assertEquals(5, userList.size());
    userList.forEach(System.out::println);
    return userList;
  }








  @RequestMapping("/select")
  public List<User> testSelect() {
    System.out.println(("----- selectAll method test ------"));
    List<User> userList = userMapper.selectList(null);
//    Assert.assertEquals(5, userList.size());
    userList.forEach(System.out::println);
    return userList;
  }

  @RequestMapping("/insert")
  public void insert() {
    System.out.println(("----- selectAll method test ------"));
    User user = new User();
    user.setId(44L);
    user.setAge(1);
    user.setEmail("zhangqinag");
    user.setName("zhangqiang");
    int insert = userMapper.insert(user);
    System.out.println(insert);
  }

  @RequestMapping("/update")
  public void update() {
    System.out.println(("----- selectAll method test ------"));
    User user = new User();
    user.setId(44L);
    user.setAge(555555);
//    user.setEmail("zhangqinag");
//    user.setName("zhangqiang");
    int insert = userMapper.updateById(user);
    System.out.println(insert);
  }

  @RequestMapping("/delete")
  public void delete() {
    System.out.println(("----- selectAll method test ------"));
    User user = new User();
    user.setId(44L);
//    user.setAge(555555);
//    user.setEmail("zhangqinag");
//    user.setName("zhangqiang");
    int insert = userMapper.deleteById(user);
    System.out.println(insert);
  }

  @RequestMapping("/selectpage")
  public Page<User> selectpage() {
    Page<User> userPage = userMapper.selectPage(new Page<>(2, 5), new QueryWrapper<>());
    System.out.println(userPage);
    return userPage;
  }

  @RequestMapping("/getAll")
  public List getAll() {
    List<User> all = userMapper.getAll();
    System.out.println(all);
    return all;
  }

  @RequestMapping("/getAllMap")
  public List getAllMap() {
    List  allMap = userMapper.getAllMap();
    System.out.println(allMap);
    return allMap;
  }


  @RequestMapping("/selectimpl")
  public List<User> selectimpl() {
    List<User> userList = userService.list();
    userList.forEach(System.out::println);
    return userList;
  }
}
