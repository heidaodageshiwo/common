package com.common.common.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.common.entity.Menu;
import com.common.common.mapper.MenuMapper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.controller
 * @ClassName: MenController
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateDate: 2022-02-10 17:21
 * @UpdateUser: zhangqiang
 * @UpdateDate: 2022-02-10 17:21
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
@RestController
public class MenController {

  @Autowired
  MenuMapper menuMapper;

  @RequestMapping("/getTree")
  public List<Menu> getTree(){
    List<Menu> menus = menuMapper.selectList(new QueryWrapper<>());
    return getMenuChildren(menus,"0000");
  }
  public List<Menu>  getMenuChildren(List<Menu> list, String pid){
    ArrayList<Menu> arrayList = new ArrayList<>();
    for (Menu menu : list) {
      if (menu.getPid().equals(pid)) {
        menu.setChildren(this.getMenuChildren(list,menu.getId()));
        arrayList.add(menu);
      }
    }
    return arrayList;
  }
}
