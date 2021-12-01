package com.common.common.flink.sql;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.types.Row;

/**
 * 批处理场景下，使用SQL语句
 */

public class DataSetSQL {

    public static void main(String[] args) throws Exception {

        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<Person> dataSource = environment.readCsvFile("data/person.txt")
                .ignoreFirstLine()
                .pojoType(Person.class, "id", "name", "age", "score", "cls");

        BatchTableEnvironment tableEnvironment = TableEnvironment.getTableEnvironment(environment);

        tableEnvironment.registerDataSet("person",dataSource);

        Table table = tableEnvironment.sqlQuery("select name, score from person where score = (select max(score) from person)");

        DataSet<Row> dataSet = tableEnvironment.toDataSet(table, Row.class);

        dataSet.print();
    }
}
