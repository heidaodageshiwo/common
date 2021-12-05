package com.common.common.postgressql.service.impl;

import com.common.common.postgressql.dao.SchoolDao;
import com.common.common.postgressql.entity.School;
import com.common.common.postgressql.service.SchoolService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (School)表服务实现类
 *
 * @author makejava
 * @since 2021-03-31 19:38:32
 */
@Service("schoolService")
public class SchoolServiceImpl implements SchoolService {
    @Resource
    private SchoolDao schoolDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public School queryById(Long id) {
        return this.schoolDao.queryById(id);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<School> queryAllByLimit(int offset, int limit) {
        return this.schoolDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param school 实例对象
     * @return 实例对象
     */
    @Override
    public School insert(School school) {
        this.schoolDao.insert(school);
        return school;
    }

    /**
     * 修改数据
     *
     * @param school 实例对象
     * @return 实例对象
     */
    @Override
    public School update(School school) {
        this.schoolDao.update(school);
        return this.queryById(school.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.schoolDao.deleteById(id) > 0;
    }
}
