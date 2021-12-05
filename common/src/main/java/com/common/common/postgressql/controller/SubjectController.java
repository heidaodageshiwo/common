package com.common.common.postgressql.controller;

import com.common.common.postgressql.entity.Subject;
import com.common.common.postgressql.service.SubjectService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Subject)表控制层
 *
 * @author makejava
 * @since 2021-03-31 19:38:34
 */
@RestController
@RequestMapping("subject")
public class SubjectController {
    /**
     * 服务对象
     */
    @Resource
    private SubjectService subjectService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public Subject selectOne(Long id) {
        return this.subjectService.queryById(id);
    }

}
