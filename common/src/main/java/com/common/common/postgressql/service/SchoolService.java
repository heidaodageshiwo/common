package com.common.common.postgressql.service;


import com.common.common.postgressql.entity.School;
import java.util.List;

/**
 * (School)表服务接口
 *
 * @author makejava
 * @since 2021-03-31 19:38:32
 */
public interface SchoolService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    School queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<School> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param school 实例对象
     * @return 实例对象
     */
    School insert(School school);

    /**
     * 修改数据
     *
     * @param school 实例对象
     * @return 实例对象
     */
    School update(School school);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
