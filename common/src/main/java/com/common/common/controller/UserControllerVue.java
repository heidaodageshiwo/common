package com.common.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.common.entity.User;
import com.common.common.mapper.UserMapper;
import com.common.common.service.UserService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@CrossOrigin
public class UserControllerVue {
  @Autowired
  private UserMapper userMapper;
  @Autowired
  private UserService userService;
  //mybatisplus分支操作 vue分支
  Map mpas=new ConcurrentHashMap<String,Object>();

  @PostMapping("/empsave")
  public void testSelectad(@RequestBody User user ) {
    if (StringUtils.isEmpty(user.getId())) {
      Random rd=new Random();
      for (int i=1;i<100;i++){
        //方式一
        long l1=(int)(Math.random()*9000+1000);
        //方式二
        long l2=rd.nextInt(9000)+1000;
      }
      user.setId((long) (rd.nextInt(9000)+1000));

      int insert = userMapper.insert(user);
    }else{
      UpdateWrapper<User> userUpdateWrapper = new UpdateWrapper<>();
      userUpdateWrapper.eq("id",user.getId());
      userMapper.update(user,userUpdateWrapper);
    }

//    System.out.println("================="+insert);
  }


  @PostMapping("/login")
  public Map<String,Object> login(@RequestBody User user, HttpSession session) {
    HashMap<String, Object> map = new HashMap<>();
    String id = session.getId();
    try {
      QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
      QueryWrapper<User> eq = userQueryWrapper.eq("name", user.getName());
      User user1 = userMapper.selectOne(eq);
      if (null!=user1) {
        map.put("success",true);
        map.put("token",id);
//        mpas.put("token",)
        return map;
      }else {
        map.put("success",false);
        map.put("token",id);
        return map;
      }

    } catch (Exception e) {
      e.printStackTrace();
      map.put("success",false);
      map.put("token",id);
//      return map;
    }
    return map;
  }

  @DeleteMapping("/token")
  public User tokens(String token) {
    System.out.println("=============="+token);
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    QueryWrapper<User> eq = userQueryWrapper.eq("id", "4");
    return userMapper.selectOne(eq);
  }

  @GetMapping("/token")
  public User token(String token) {
    System.out.println("=============="+token);
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    QueryWrapper<User> eq = userQueryWrapper.eq("id", "4");
    return userMapper.selectOne(eq);
  }

  @DeleteMapping("/emp/{id}")
  public void testSelectd(@PathVariable("id") Integer id ) {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.eq("id",id);
    userMapper.delete(userQueryWrapper);
//    User user = userMapper.selectOne(userQueryWrapper);
//    return user;
  }

  @GetMapping("/emps")
  public List<User> testSelects( ) {
    return userMapper.selectList(new QueryWrapper<>());
  }
  @GetMapping("/emp/{id}")
  public User testSelect(@PathVariable("id") Integer id ) {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.eq("id",id);
    User user = userMapper.selectOne(userQueryWrapper);
    return user;
  }

  @GetMapping("/emp")
  public User testSelecta(Integer id ) {
    QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
    userQueryWrapper.eq("id",id);
    User user = userMapper.selectOne(userQueryWrapper);
    return user;
  }
}






























