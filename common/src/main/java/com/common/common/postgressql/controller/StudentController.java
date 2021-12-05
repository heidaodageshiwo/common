package com.common.common.postgressql.controller;

import com.common.common.postgressql.entity.Student;
import com.common.common.postgressql.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Student)表控制层
 *
 * @author makejava
 * @since 2021-03-31 19:38:33
 */
@RestController
@RequestMapping("student")
public class StudentController {
    /**
     * 服务对象
     */
    @Resource
    private StudentService studentService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne/{id}")
    public Student selectOne(@PathVariable("id") Long id) {
        return this.studentService.queryById(id);
    }


    @GetMapping
    public ResponseEntity<Object> list(int pageSize, int pageNum) {
        return ResponseEntity.ok(studentService.list(pageSize, pageNum));
    }

}
