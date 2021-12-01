package com.common.common.flink.sql;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

import java.util.Arrays;

/**
 * 流处理场景下，使用SQL语句
 *
 作为流式查询结果的表将动态更新，它随着新记录到达查询的输入流而改变，于是，转换到这样的动态查询DataStream 需要对表的更新进行编码。

 将表转换为DataStream有两种模式：

 Append Mode：这种模式仅用于动态表仅仅通过INSERT来进行表的更新，它是仅可追加模式， 并且之前输出的表不会进行更改
 Retract Mode：这种模式经常用到。它使用布尔值的变量来对INSERT和DELETE对表的更新做标记
 */

public class StreamingSQL {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();

        environment.setParallelism(1);
//        DataStreamSource<String> dataStreamSource = environment.fromCollection(Arrays.asList(
//                "1,xiaoming,18,90,1",
//                "2,xiaodong,20,91,1",
//                "3,xiaohong,19,69,2",
//                "4,xiaogang,18,78,2",
//                "5,xiaoying,18,100,2"
//        ));

        DataStreamSource<String> dataStreamSource = environment.socketTextStream("bd1701", 9999);

        SingleOutputStreamOperator<Person> map = dataStreamSource.map(new MapFunction<String, Person>() {
            @Override
            public Person map(String line) throws Exception {
                String[] split = line.split(",");
                return new Person(split[0], split[1], Integer.valueOf(split[2]), Integer.valueOf(split[3]), split[4]);
            }
        });

        StreamTableEnvironment tableEnvironment = TableEnvironment.getTableEnvironment(environment);

        tableEnvironment.registerDataStream("person",map);
        //查询出分数大于90的人。对于新的输入流数据 只要分出大于90，就一直输出，此种情况下输出结果是一直追加的
//        Table table = tableEnvironment.sqlQuery("select * from person where score > 90");
        //查询出分数最大的那个人。此种情况下，随着新的输入流数据进来，最大的分数的那个人结果可能会发生变化，所以应该用Retract 模式
        Table table = tableEnvironment.sqlQuery("select name, score from person where score = (select max(score) from person)");

//
//        DataStream<Row> dataStream = tableEnvironment.toAppendStream(table, Row.class);

        //返回true代表数据插入，false代表数据的撤回
        DataStream<Tuple2<Boolean, Row>> dataStream = tableEnvironment.toRetractStream(table, Row.class);


        dataStream.print();

        environment.execute();
    }
}
