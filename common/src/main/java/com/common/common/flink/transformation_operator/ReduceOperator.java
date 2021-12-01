package com.common.common.flink.transformation_operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class ReduceOperator {
    private static final String[] WORDS = new String[]{
            "hello shsxt haha heihei",
            "hello bjsxt word flink",
            "hello flink spark hadoop"
    };

    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置并行度
        env.setParallelism(1);
        //通过数组创建source
        DataStreamSource<String> dataStreamSource = env.fromElements(WORDS);

        SingleOutputStreamOperator<Tuple2<String, Integer>> singleOutputStreamOperator = dataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");
                for (String word : split) {
                    Thread.sleep(1000);
                    System.err.println("key :" +  word + " value : " + 1);
                    out.collect(new Tuple2<>(word, 1));
                }
            }
        });

        singleOutputStreamOperator
                .keyBy(0)
                .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                        System.out.println("value1:"+value1 +" value2: "+ value2);
                        return new Tuple2<>(value1.f0,value1.f1+value2.f1);
                    }
                })
                .print();

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
