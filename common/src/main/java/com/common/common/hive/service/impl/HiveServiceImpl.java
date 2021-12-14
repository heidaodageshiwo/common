package com.common.common.hive.service.impl;
import com.common.common.hive.service.HiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class HiveServiceImpl implements HiveService {

    @Autowired
    @Qualifier("hiveJdbcTemplate")
    private JdbcTemplate hiveJdbcTemplate;

    @Autowired
    @Qualifier("hiveDruidDataSource")
    private DataSource hiveDruidDataSource;

    @Override
    public Object select(String hql) {
        return hiveJdbcTemplate.queryForObject(hql, Object.class);
    }

    @Override
    public String loadInfoTable(){
        String filePath = "/root/value/student.txt";
        String sql ="load data local inpath '"+ filePath +"' into table studentNew";
        String result = "load data into table studentNew" ;
        try{
            hiveJdbcTemplate.execute(sql);

        }catch (Exception e){
            log.info("exce:e{}",e.getMessage());
            result="load data  fiald";
        }
        return result;
    }

    @Override
    public String createTable(){
        StringBuffer sql = new StringBuffer();
        /*sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(" studentNew ");
        sql.append(" ( id String comment '编号', ");
        sql.append(" name String comment '名字') ");
        sql.append("ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' "); // 定义分隔符
        // 作为文本存储
        sql.append("STORED AS TEXTFILE");*/
        sql.append(" create table test123 (id int)");
        log.info("Running: " + sql);
        String result = "Create table successfully...";
        try {
            // hiveJdbcTemplate.execute(sql.toString());
            hiveJdbcTemplate.execute(sql.toString());
        } catch (DataAccessException dae) {
            result = "Create table encounter an error: " + dae.getMessage();
            log.error(result);
        }
        return result;
    }


    @Override
    public List<String> listAllTables() {
        List<String> result = new ArrayList<>();
        try {
            Statement statement = hiveDruidDataSource.getConnection().createStatement();
            String sql = "show tables";
            log.info("Running: " + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public List<String> describeTable(String tableName) {
        if (StringUtils.isEmpty(tableName)){
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        try {
            Statement statement = hiveDruidDataSource.getConnection().createStatement();
            String sql = "describe " + tableName;
            log.info("Running" + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                result.add(resultSet.getString(1));
            }
            return result;
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> selectFromTable(String tableName) {
        if (StringUtils.isEmpty(tableName)){
            return Collections.emptyList();
        }
        List<String> result = new ArrayList<>();
        try {
            Statement statement = hiveDruidDataSource.getConnection().createStatement();
            String sql = "select * from " + tableName;
            log.info("Running" + sql);
            ResultSet resultSet = statement.executeQuery(sql);
            int columnCount = resultSet.getMetaData().getColumnCount();
            String str = null;
            while (resultSet.next()) {
                str = "";
                for (int i = 1; i < columnCount; i++) {
                    str += resultSet.getString(i) + " ";
                }
                str += resultSet.getString(columnCount);
                log.info(str);
                result.add(str);
            }
            return result;
        } catch (SQLException throwables) {
            log.error(throwables.getMessage());
        }
        return Collections.emptyList();
    }



}

