package com.common.common.postgressql.controller;

import com.common.common.postgressql.entity.StudentSubject;
import com.common.common.postgressql.service.StudentSubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (StudentSubject)表控制层
 *
 * @author makejava
 * @since 2021-03-31 19:38:33
 */
@RestController
@RequestMapping("studentSubject")
public class StudentSubjectController {
    /**
     * 服务对象
     */
    @Resource
    private StudentSubjectService studentSubjectService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public StudentSubject selectOne(Long id) {
        return this.studentSubjectService.queryById(id);
    }

}
