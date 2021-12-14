package com.common.common.hive.service;

import java.util.List;

public interface HiveService {

    Object select(String hql);

    List<String> listAllTables();

    List<String> describeTable(String tableName);

    List<String> selectFromTable(String tableName);

    String loadInfoTable();

    String createTable();

}

