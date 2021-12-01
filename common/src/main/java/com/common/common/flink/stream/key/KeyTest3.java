package com.common.common.flink.stream.key;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 自定义key选择器来制定虚拟的key
 * socket数据
 * jiaqi m 51 3 10 40 0
 * zhiyu m 50 2 30 10 0
 * zhisheng f 60 6 30 30 30
 */
public class KeyTest3 {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        DataStreamSource<String> node01 = env.socketTextStream("bd1701", 9999);

        KeyedStream<String, String> keyBy = node01.keyBy(new KeySelector<String, String>() {
            @Override
            public String getKey(String value) throws Exception {
                return value.split(" ")[1];
            }
        });

        keyBy.timeWindow(Time.seconds(5)).reduce(new ReduceFunction<String>() {
            @Override
            public String reduce(String value1, String value2) throws Exception {
                return value1 + "#" + value2;
            }
        }).print();

        try {
            env.execute("keyselector");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
