package com.common.common.flink.stream.test;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 *  开启与禁用工作链时，输出的结果不一样。
 *  当开启工作链时(默认启动)，operator map1与map2 组成一个task.
 *     此时task运行时，对于hello，flink 这两条数据是：
 *     先打印 hello ---- 1 , hello->1 ---- 2
 *     后打印 flink ---- 1 , flink->1 ---- 2
 *  当禁用工作链时，operator map1与map2 分别在两个task中执行
 *     此时task运行时，对于hello，flink 这两条数据是：
 *     先打印 hello ---- 1 , flink ---- 1
 *     后打印 hello->1 ---- 2  , flink->1 ---- 2
 *
 *  注：操作链类似spark的管道,一个task执行多个的算子.
 */
public class OperatorChainTest {

    public static final String[] WORDS = new String[] {
            "hello",
            "flink",
            "spark",
            "hbase"
    };

    public static void main(String[] args) {
        //设置执行环境，类似spark中初始化sparkcontext一样
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);

        //全局，默认开启
//        env.disableOperatorChaining();



        DataStreamSource<String> dataStreamSource = env.fromElements(WORDS);

        SingleOutputStreamOperator<String> map = dataStreamSource.map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                System.out.println(value + "-11111111111111");
                return value;
            }
        }).disableChaining().map(new MapFunction<String, String>() {
            @Override
            public String map(String value) throws Exception {
                System.out.println(value + "-222222222222222");
                return value;
            }
        });





        map.printToErr();
        //局部的设置：记住不能以source操作符后去开启链。
//        dataStreamSource.startNewChain()  ×
//        map.startNewChain()


        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
