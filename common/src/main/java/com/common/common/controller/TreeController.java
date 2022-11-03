package com.common.common.controller;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.controller
 * @ClassName: TreeController
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-11-03  15:17
 * @UpdateDate: 2022-11-03  15:17
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import com.common.common.tree.TreeBuild;
import com.common.common.tree.TreeNode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  TreeController 树控制层
 *  方式：传递所有数据集合作为参数，调用buildTree()构建树形。
 *
 *  @author LBF
 *  @date 2022/1/10 17:49
 */
@RestController
@RequestMapping("/tree")
public class TreeController {

    @GetMapping("/treeTest")
    public Map treeTest(){

        // 模拟测试数据（通常为数据库的查询结果）
        List<TreeNode> treeNodeList = new ArrayList<>();
        treeNodeList.add(new TreeNode(1,0,"顶级节点A"));
        treeNodeList.add(new TreeNode(2,0,"顶级节点B"));
        treeNodeList.add(new TreeNode(3,1,"父节点是A"));
        treeNodeList.add(new TreeNode(4,2,"父节点是B"));
        treeNodeList.add(new TreeNode(5,2,"父节点是B"));
        treeNodeList.add(new TreeNode(6,3,"父节点的ID是3"));

        // 创建树形结构（数据集合作为参数）
        TreeBuild treeBuild = new TreeBuild(treeNodeList);
        // 原查询结果转换树形结构
        treeNodeList = treeBuild.buildTree();
        // AjaxResult：个人封装返回的结果体
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
         objectObjectHashMap.put("测试数据",treeNodeList);
        return objectObjectHashMap;
    }
}