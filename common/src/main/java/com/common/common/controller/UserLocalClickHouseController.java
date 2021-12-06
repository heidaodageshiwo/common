package com.common.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.common.common.entity.User;
import com.common.common.entity.UserLocal;
import com.common.common.mapper.UserLocalMapper;
import com.common.common.mapper.UserMapper;
import com.common.common.service.UserService;
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
public class UserLocalClickHouseController {

  @Autowired
  private UserLocalMapper userLocalMapper;


  //mybatisplus分支操作
  @RequestMapping("/selectclickHouse")
  public List<UserLocal> testSelect() {
    System.out.println(("----- selectAll method test ------"));
    List<UserLocal> userList = userLocalMapper.selectList(null);
//    Assert.assertEquals(5, userList.size());
    userList.forEach(System.out::println);
    return userList;
  }

}
