package com.common.common.postgressql.controller;

import com.common.common.postgressql.entity.Test;
import com.common.common.postgressql.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * (Test)表控制层
 *
 * @author makejava
 * @since 2021-03-31 16:55:00
 */
@RestController
@RequestMapping("test")
public class TestController {
    /**
     * 服务对象
     */
    @Resource
    private TestService testService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> selectOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(testService.queryById(id));
    }

    @GetMapping
    public ResponseEntity<Object> list() {
        return ResponseEntity.ok(testService.queryAllByLimit(0, 99999));
    }


    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Test test) {
        testService.insert(test);
        return ResponseEntity.ok(test);
    }

    @PutMapping
    public ResponseEntity<Object> update(@RequestBody Test test) {
        testService.update(test);
        return ResponseEntity.ok(test);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        testService.deleteById(id);
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

}
