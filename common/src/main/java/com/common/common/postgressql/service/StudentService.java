package com.common.common.postgressql.service;


import com.common.common.postgressql.entity.Student;
import com.common.common.postgressql.entity.dto.StudentDTO;
import java.util.List;

/**
 * (Student)表服务接口
 *
 * @author makejava
 * @since 2021-03-31 19:38:33
 */
public interface StudentService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Student queryById(Long id);


    /**
     * 新增数据
     *
     * @param student 实例对象
     * @return 实例对象
     */
    Student insert(Student student);

    /**
     * 修改数据
     *
     * @param student 实例对象
     * @return 实例对象
     */
    Student update(Student student);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

    List<StudentDTO> list(int pageSize, int pageNum);
}
