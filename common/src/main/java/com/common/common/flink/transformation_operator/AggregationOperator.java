package com.common.common.flink.transformation_operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Random;

public class AggregationOperator {


    public static final String[] WORDS = new String[]{
            "hello shsxt",
            "nihao joy",
            "hello shsxt"
    };

    public static void main(String[] args) {
        //创建本地运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度
        env.setParallelism(1);
        //通过数组创建source
        DataStreamSource<String> dataStreamSource = env.fromElements(WORDS);

        SingleOutputStreamOperator<Tuple2<String, Integer>> singleOutputStreamOperator = dataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");
                Random random = new Random();
                for (String word : split) {
                    Thread.sleep(1000);
                    int num = random.nextInt(100);
                    System.err.println(System.currentTimeMillis() + "Key " + word + " value " + num);
                    out.collect(new Tuple2<>(word, num));
                }
            }
        });

        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = singleOutputStreamOperator.keyBy(0);

//        keyedStream.reduce(new ReduceFunction<Tuple2<String, Integer>>() {
//            @Override
//            public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
//                return new Tuple2<>(value1.f0, value1.f1 + value2.f1);
//            }
//        }).print();
//        keyedStream.sum(1).print();
//        keyedStream.min(1).print();
//        keyedStream.max(1).print();
        keyedStream.minBy(1).print();
//        keyedStream.maxBy(1).print();


        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
