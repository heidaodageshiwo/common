package com.common.common.flink.batch;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class StreamCounterTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> socketTextStream = env.socketTextStream("node01", 9999);

        socketTextStream.map(new RichMapFunction<String, String>() {
            private IntCounter intCounter = new IntCounter();

            @Override
            public void open(Configuration parameters) throws Exception {
                getRuntimeContext().addAccumulator("streamAcc",this.intCounter);
            }

            @Override
            public String map(String value) throws Exception {
                this.intCounter.add(1);
                return value;
            }
        }).print();

        JobExecutionResult jobExecutionResult = env.execute("streamAcc1");

        Integer acc = jobExecutionResult.getAccumulatorResult("streamAcc");

        System.out.println(acc);

    }
}
