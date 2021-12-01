package com.common.common.flink.stream.source_operator;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.List;

/**
 * 从集合中创建source
 */
public class Source_FromCollection {


    public static void main(String[] args) {



        //设置执行环境，类似spark中初始化sparkcontext一样
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        List<String> list = Arrays.asList("hello joy", "hello zhiduoxing", "hello jiejie");
        DataStreamSource<String> dataStreamSource = env.fromCollection(list);
        SingleOutputStreamOperator<Tuple2<String, Integer>> singleOutputStreamOperator = dataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                for (String word : value.split(" ")) {
                    out.collect(new Tuple2<>(word, 1));
                }
            }
        });
        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = singleOutputStreamOperator.keyBy(0);
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);
        sum.print();

        try {
            env.execute("wordCount");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
