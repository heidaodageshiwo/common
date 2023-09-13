package com.common.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.common.entity.SysDeptMenu;
import com.common.common.mapper.SysDeptMenuMapper;
import com.common.common.tree.TreeSelect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @PackageName com.lnsoft.bygl.service
 * @ClassName SysDeptMenuService
 * @Description TODO
 * @Author zhangqiang
 * @Date 2023/2/28 8:47
 * @Version 1.0
 **/
@Service
public class SysDeptMenuService {

    @Autowired(required = false)
    private SysDeptMenuMapper sysDeptMenuMapper;

    public List<SysDeptMenu> selectDeptList() {

        List<SysDeptMenu> sysDeptMenus = sysDeptMenuMapper.selectList(new QueryWrapper<>());
        return sysDeptMenus;
    }

    public List<TreeSelect> buildDeptTreeSelect(List<SysDeptMenu> depts) {
        List<SysDeptMenu> deptTrees = buildDeptTree(depts);
        return deptTrees.stream().map(TreeSelect::new).collect(Collectors.toList());
    }

    public List<SysDeptMenu> buildDeptTree(List<SysDeptMenu> depts) {
        List<SysDeptMenu> returnList = new ArrayList<SysDeptMenu>();
        List<Long> tempList = new ArrayList<Long>();
        for (SysDeptMenu dept : depts) {
            tempList.add(dept.getDeptId());
        }
        for (Iterator<SysDeptMenu> iterator = depts.iterator(); iterator.hasNext(); ) {
            SysDeptMenu dept = (SysDeptMenu) iterator.next();
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(dept.getParentId())) {
                recursionFn(depts, dept);
                returnList.add(dept);
            }
        }
        if (returnList.isEmpty()) {
            returnList = depts;
        }
        return returnList;
    }

    private void recursionFn(List<SysDeptMenu> list, SysDeptMenu t) {
        // 得到子节点列表
        List<SysDeptMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysDeptMenu tChild : childList) {
            if (hasChild(list, tChild)) {
                recursionFn(list, tChild);
            }
        }
    }

    private List<SysDeptMenu> getChildList(List<SysDeptMenu> list, SysDeptMenu t) {
        List<SysDeptMenu> tlist = new ArrayList<SysDeptMenu>();
        Iterator<SysDeptMenu> it = list.iterator();
        while (it.hasNext()) {
            SysDeptMenu n = (SysDeptMenu) it.next();
//            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDeptId().longValue()) {
            if (null !=n.getParentId() && n.getParentId().longValue() == t.getDeptId().longValue()) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysDeptMenu> list, SysDeptMenu t) {
        return getChildList(list, t).size() > 0 ? true : false;
    }
}
