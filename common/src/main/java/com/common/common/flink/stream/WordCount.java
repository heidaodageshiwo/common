package com.common.common.flink.stream;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class WordCount {

    public static void main(String[] args) throws Exception {
        //1.初始化环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //2.读取数据源，并进行转换操作
        DataStream<Tuple2<String, Integer>> dataStream = env
                .socketTextStream("bd1701", 9999)
                .flatMap(new Splitter())
                .keyBy(0)
                //每5秒触发一批计算
                .timeWindow(Time.seconds(5))
                .sum(1);
        //3.将结果进行输出
        dataStream.print();
        //4.流式计算需要手动触发执行操作，
        env.execute("Window WordCount");
    }

    public static class Splitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String sentence, Collector<Tuple2<String, Integer>> out) throws Exception {
            for (String word: sentence.split(" ")) {
                out.collect(new Tuple2<String, Integer>(word, 1));
            }
        }
    }
}
