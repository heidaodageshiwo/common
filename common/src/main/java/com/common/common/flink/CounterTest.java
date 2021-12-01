package com.common.common.flink;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author Mr.Jaden
 * @date 2020/11/7 16:30
 * @description 每天起床第一句，先给自己打个气
 */
public class CounterTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        DataStreamSource<String> stream = env.socketTextStream("bd1701", 9999);
        stream.map(new RichMapFunction<String, String>() {

            IntCounter intCounter =  new IntCounter();
            @Override
            public void open(Configuration parameters) throws Exception {
                getRuntimeContext().addAccumulator("myACC",intCounter);
            }

            @Override
            public String map(String value) throws Exception {
                intCounter.add(1);
                return value;
            }
        }).setParallelism(2).print();

        JobExecutionResult result = env.execute();

        Integer myACC = result.getAccumulatorResult("myACC");

        System.out.println(myACC);

    }
}
