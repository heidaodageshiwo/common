package com.common.common.flink.stream.source_operator;


import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Source_GenerateSequence {


    public static void main(String[] args) {
        //设置执行环境，类似spark中初始化sparkcontext一样
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //设置任务并行度为1，即1个task;本地运行的时候默认8个并行任务。
        env.setParallelism(1);

        //获取数据源。
        DataStreamSource<Long> dataStreamSource = env.generateSequence(0, 100);

        SingleOutputStreamOperator<Long> filter = dataStreamSource.filter(new FilterFunction<Long>() {
            @Override
            public boolean filter(Long value) throws Exception {
return true;
//                return value % 3 == 0;
            }
        });



        filter.print();

        try {
            //转换算子都是懒执行的，最后要显示调用 执行程序，
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
