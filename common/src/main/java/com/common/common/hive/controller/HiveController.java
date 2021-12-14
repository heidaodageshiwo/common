package com.common.common.hive.controller;

import com.common.common.hive.service.HiveService;
import org.apache.tools.ant.taskdefs.condition.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/hive")
public class HiveController {

    @Resource
    HiveService hiveService;

    @GetMapping(value = "/cs")
    public ResponseEntity getTs(){
        ResponseEntity responseEntity = new ResponseEntity("{ceshi}",HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(value = "/find")
    public ResponseEntity getFind(HttpServletRequest request){
        String tableName = request.getParameter("tableName");
        List<String> strings = hiveService.selectFromTable(tableName);
        ResponseEntity responseEntity = new ResponseEntity(strings,HttpStatus.OK);
        return responseEntity;
    }

    @GetMapping(value = "/create")
    public ResponseEntity createData(HttpServletRequest request){
        String table = hiveService.createTable();
        ResponseEntity responseEntity = new ResponseEntity(table,HttpStatus.OK);
        return responseEntity;
    }


    @GetMapping(value = "/load")
    public ResponseEntity loadData(HttpServletRequest request){
        System.out.println("111111111");
        String s = hiveService.loadInfoTable();
        ResponseEntity responseEntity = new ResponseEntity(s,HttpStatus.OK);
        return responseEntity;
    }






}
