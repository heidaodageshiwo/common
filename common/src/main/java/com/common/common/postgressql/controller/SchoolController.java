package com.common.common.postgressql.controller;

import com.common.common.postgressql.entity.School;
import com.common.common.postgressql.service.SchoolService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (School)表控制层
 *
 * @author makejava
 * @since 2021-03-31 19:38:32
 */
@RestController
@RequestMapping("school")
public class SchoolController {
    /**
     * 服务对象
     */
    @Resource
    private SchoolService schoolService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("selectOne")
    public School selectOne(Long id) {
        return this.schoolService.queryById(id);
    }

}
