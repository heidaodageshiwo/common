package com.common.common.flink.sql;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * 使用 table API 来操作数据
 */

public class TableApi {

    public static void main(String[] args) {


        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();

        //获取批处理的SQL执行环境
        BatchTableEnvironment tableEnvironment = TableEnvironment.getTableEnvironment(environment);

        //读取CSV格式的数据，并转换成Person类型
        DataSource<Person> dataSource = environment.readCsvFile("data/person.txt")
                .ignoreFirstLine()//csv往往第一行会有字段信息
                .pojoType(Person.class, "id", "name", "age", "score", "cls");
        //注册
        tableEnvironment.registerDataSet("person",dataSource);
        //环境操作
        Table mytable = tableEnvironment.scan("person");
        //打印表的schema信息
        mytable.printSchema();
        //原生api操作
//        Table result = mytable.select("id,name,age,score,cls").where("score > 90");
//        DataSet<Person> dataSet = tableEnvironment.toDataSet(result, Person.class);
        //计算出每班级最高的分数
        Table result = mytable.groupBy("cls").select("cls,score.max");

        DataSet<Row> dataSet = tableEnvironment.toDataSet(result, Row.class);

        try {
            dataSet.print();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
