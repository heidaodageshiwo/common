package com.common.common.controller;

import com.common.common.entity.SysDeptMenu;
import com.common.common.service.SysDeptMenuService;
import com.common.common.tree.AjaxResultzq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @PackageName com.lnsoft.bygl.controller
 * @ClassName TestController
 * @Description TODO
 * @Author zhangqiang
 * @Date 2023/2/28 8:46
 * @Version 1.0
 **/
@RestController
@RequestMapping("/zq")
public class SysDeptMenuController {
    @Autowired
    public SysDeptMenuService sysDeptMenuService;
//    http://localhost:8080/zq/treeselect
    @GetMapping("/treeselect")
    public AjaxResultzq treeselect()
    {
        List<SysDeptMenu> depts = sysDeptMenuService.selectDeptList();
        return AjaxResultzq.success(sysDeptMenuService.buildDeptTreeSelect(depts));
    }
}
