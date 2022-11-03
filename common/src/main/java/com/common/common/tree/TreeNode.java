package com.common.common.tree;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.tree
 * @ClassName: TreeNode
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-11-03  15:16
 * @UpdateDate: 2022-11-03  15:16
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *  TreeNode 树节点 （定义每一个节点的信息，即每一个节点对应一条数据信息）
 *
 *  @author LBF
 *  @date 2022/1/10 16:46
 */
@Setter
@Getter
public class TreeNode {

    /** 节点ID */
    private Integer id;

    /** 父节点ID：顶级节点为0 */
    private Integer parentId;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    private List<TreeNode> children;

    public TreeNode(Integer id, Integer parentId, String label) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }
}