package com.common.common.postgressql.service.impl;

import com.common.common.postgressql.dao.StudentDao;
import com.common.common.postgressql.entity.Student;
import com.common.common.postgressql.entity.dto.StudentDTO;
import com.common.common.postgressql.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (Student)表服务实现类
 *
 * @author makejava
 * @since 2021-03-31 19:38:33
 */
@Service("studentService")
public class StudentServiceImpl implements StudentService {
    @Resource
    private StudentDao studentDao;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Override
    public Student queryById(Long id) {
        return this.studentDao.queryById(id);
    }



    /**
     * 新增数据
     *
     * @param student 实例对象
     * @return 实例对象
     */
    @Override
    public Student insert(Student student) {
        this.studentDao.insert(student);
        return student;
    }

    /**
     * 修改数据
     *
     * @param student 实例对象
     * @return 实例对象
     */
    @Override
    public Student update(Student student) {
        this.studentDao.update(student);
        return this.queryById(student.getId());
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long id) {
        return this.studentDao.deleteById(id) > 0;
    }

    @Override
    public List<StudentDTO> list(int pageSize, int pageNum) {
        int limit = pageSize;
        int offset = (pageNum - 1) * pageSize;
        return studentDao.queryAllByLimit(limit, offset);
    }
}
