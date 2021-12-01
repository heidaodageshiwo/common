package com.common.common.flink.stream.sink_operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.util.Arrays;
import java.util.List;

public class Sink_WriteAsText {

    public static void main(String[] args) {
        //设置执行环境，类似spark中初始化sparkcontext一样
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(3);

        List<String> list = Arrays.asList("hello xixi", "hello xixi", "hello xixi");
        //获取数据源。
        DataStreamSource<String> dataStreamSource = env.fromCollection(list);

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = dataStreamSource
                .flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");
                for (String word : split) {
                    Thread.sleep(5000);

                    out.collect(new Tuple2<>(word, 1));
                }
            }
        })
                .keyBy(0)
                .sum(1);

        /**
         *  WriteMode.OVERWRITE; 覆盖
         *  WriteMode.NO_OVERWRITE; 不覆盖
         *  注意：当并行度>1时   env.setParallelism(2); 会writeAsText.txt目录，该目录下产生对应的task_id文件
         *       当并行度=1时   env.setParallelism(1); 会writeAsText.txt文件
         */

        sum.writeAsText("./data/writeAsText.txt", FileSystem.WriteMode.OVERWRITE);

        sum.writeAsCsv("./data/writeAsText.csv", FileSystem.WriteMode.OVERWRITE);

        try {
            //转换算子都是懒执行的，最后要显示调用 执行程序，
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
