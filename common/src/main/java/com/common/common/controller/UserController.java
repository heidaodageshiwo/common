package com.common.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.common.entity.User;
import com.common.common.mapper.UserMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

  //mybatisplus分支操作
  @RequestMapping("/select")
  public List<User>  testSelect() {
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
  public  Page<User> selectpage() {
    Page<User> userPage = userMapper.selectPage(new Page<>(2, 5), new QueryWrapper<>());
    System.out.println(userPage);
    return userPage;
  }

}
