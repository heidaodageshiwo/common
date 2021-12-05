package com.common.common.postgressql.service.impl;

import com.common.common.postgressql.dao.StudentSubjectDao;
import com.common.common.postgressql.entity.StudentSubject;
import com.common.common.postgressql.service.StudentSubjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * (StudentSubject)表服务实现类
 *
 * @author makejava
 * @since 2021-03-31 19:38:33
 */
@Service("studentSubjectService")
public class StudentSubjectServiceImpl implements StudentSubjectService {
    @Resource
    private StudentSubjectDao studentSubjectDao;

    /**
     * 通过ID查询单条数据
     *
     * @param studentId 主键
     * @return 实例对象
     */
    @Override
    public StudentSubject queryById(Long studentId) {
        return this.studentSubjectDao.queryById(studentId);
    }

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    @Override
    public List<StudentSubject> queryAllByLimit(int offset, int limit) {
        return this.studentSubjectDao.queryAllByLimit(offset, limit);
    }

    /**
     * 新增数据
     *
     * @param studentSubject 实例对象
     * @return 实例对象
     */
    @Override
    public StudentSubject insert(StudentSubject studentSubject) {
        this.studentSubjectDao.insert(studentSubject);
        return studentSubject;
    }

    /**
     * 修改数据
     *
     * @param studentSubject 实例对象
     * @return 实例对象
     */
    @Override
    public StudentSubject update(StudentSubject studentSubject) {
        this.studentSubjectDao.update(studentSubject);
        return this.queryById(studentSubject.getStudentId());
    }

    /**
     * 通过主键删除数据
     *
     * @param studentId 主键
     * @return 是否成功
     */
    @Override
    public boolean deleteById(Long studentId) {
        return this.studentSubjectDao.deleteById(studentId) > 0;
    }
}
