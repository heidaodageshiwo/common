package com.common.common.flink.stream.sink_operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.List;

public class Sink_WriteToSocket {

    public static void main(String[] args) {
        //设置执行环境，类似spark中初始化sparkcontext一样
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(3);

        List<String> list = Arrays.asList("hello xixi", "hehe xixi", "mm huhu", "hello xixi");
        //获取数据源。
        DataStreamSource<String> dataStreamSource = env.fromCollection(list);
        SingleOutputStreamOperator<Tuple2<String, Integer>> pairStream = dataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");
                for (String word : split) {
//                    Thread.sleep(5000);

                    out.collect(new Tuple2<>(word, 1));
                }
            }
        });
        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = pairStream.keyBy(0);
        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);

        sum.writeToSocket("bd1701", 9999, new SerializationSchema<Tuple2<String, Integer>>() {
            @Override
            public byte[] serialize(Tuple2<String, Integer> element) {
                String key = element.f0;
                Integer value = element.f1;
                String output = key + "," + value + "\r\n";
                return output.getBytes();
            }
        });


        try {
            //转换算子都是懒执行的，最后要显示调用 执行程序，
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
