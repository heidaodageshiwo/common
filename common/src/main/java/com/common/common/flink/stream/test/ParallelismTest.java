package com.common.common.flink.stream.test;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSink;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * 并行度测试:
 *   1.当local模式运行时，任务并行度默认为计算机的逻辑处理器个数，
 *     可以设置成其他的，通过env.setParallelism(3)设置，
 *     此时整个程序的 source,tarnsformation operator,sink 并行度都是3。
 *     注：这是执行环境级别的并行度设置
 *
 *   2.若要覆盖执行环境级别设置的并行度，可以设置指定的operator并行度，调用对应operator的setParallelism方法。
 *
 *  3.注意：有的source并行度只能是1，调不了，比如:
 *           env.fromElements
 *           env.fromCollections
 *           env.socketTextStream
 *           只有实现ParallelSourceFunction接口的datasource才可以调整并行度。
 */
public class ParallelismTest {

    public static final String[] WORDS = new String[] {
            "hello",
            "flink",
            "spark",
            "hbase"

    };

    public static void main(String[] args) {
        //设置执行环境，类似spark中初始化sparkcontext一样
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(2);

        //通过 env.fromElements(WORDS)创建的datasource 无法设置并行度。
//        DataStreamSource<String> dataStreamSource = env.fromElements(WORDS);
        DataStreamSource<String> dataStreamSource = env.readTextFile("./data/textfile");
//       Source: 1 is not a parallel source如果是数组的话
        dataStreamSource.setParallelism(3);

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
//        pairStream.setParallelism(5);

        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = pairStream.keyBy(0);


        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);
        sum.setParallelism(3);

        //设置keyed aggregatetion 算子并行度。
        sum.printToErr();

        //设置print sink 的并行度...
//        sum.printToErr().setParallelism(5);

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
