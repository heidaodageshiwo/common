package com.common.common.flink;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author Mr.Jaden
 * @date 2020/11/5 11:29
 * @description 每天起床第一句，先给自己打个气
 */
public class StreamWC {
    public static void main(String[] args) throws Exception {
        //1.初始化环境
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
       StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(
            new Configuration());
        env.setParallelism(1);
        //source
        //transformations
        DataStream<Tuple2<String, Integer>> sum = env
                .socketTextStream("127.0.0.1", 9999)
                .flatMap(new TestWC.LineToWordOne())
                .keyBy(0)
//                .timeWindow(Time.seconds(5))
                .sum(1);
//        sinks
        sum.print();
        //流式计算需要手动执行触发上述操作
        env.execute("wc");



    }
}
