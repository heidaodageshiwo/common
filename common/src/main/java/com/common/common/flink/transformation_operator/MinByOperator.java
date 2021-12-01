package com.common.common.flink.transformation_operator;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.ArrayList;
import java.util.List;

public class MinByOperator {
    public static void main(String[] args) {
        //创建本地运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        List<Tuple3<String, String, Integer>> list = new ArrayList<>();
        list.add(new Tuple3<>("男","老王",80));
        list.add(new Tuple3<>("男","小王",20));
        list.add(new Tuple3<>("男","老李",8000));
        list.add(new Tuple3<>("男","小李",10));

        DataStreamSource<Tuple3<String, String, Integer>> dataStreamSource = env.fromCollection(list);

        dataStreamSource.keyBy(0).minBy(2).print();
//        dataStreamSource.keyBy(0).min(2).print();

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
