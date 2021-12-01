package com.common.common.flink.stream.sink_operator;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.io.OutputFormat;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Sink_WriteUsingOutputFormat {

    public static void main(String[] args) {
        //设置执行环境，类似spark中初始化sparkcontext一样
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(4);

        List<String> list = Arrays.asList("hello xixi", "hehe xixi", "mm huhu");
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

        sum.printToErr();
        //模拟printToErr()


        sum.writeUsingOutputFormat(new OutputFormat<Tuple2<String, Integer>>() {
            private String prefix = null;

            //2> (hello,1)
            //如果不需要配置可以不写
            @Override
            public void configure(Configuration parameters) {

            }

            /**
             * 初始化防范
             * @param taskNumber    task_id
             * @param numTasks  总共task个数
             * @throws IOException
             */
            @Override
            public void open(int taskNumber, int numTasks) throws IOException {
                prefix = taskNumber + 1 +"> ";
            }
            //最终输出
            @Override
            public void writeRecord(Tuple2<String, Integer> record) throws IOException {
                System.err.println(prefix + record);
            }

            @Override
            public void close() throws IOException {

            }
        });


//        System.err.println();

//        sum.writeUsingOutputFormat(new OutputFormat<Tuple2<String, Integer>>() {
//            private String prefix = null;
//
//            @Override
//            public void configure(Configuration parameters) {
//
//            }
//
//            /**
//             * 初始化方法
//             * @param taskNumber
//             * @param numTasks
//             * @throws IOException
//             */
//            @Override
//            public void open(int taskNumber, int numTasks) throws IOException {
//                System.err.println("taskNumber"+taskNumber+",numTasks :" + numTasks);
//                prefix = taskNumber + 1 + "> ";
//            }
//
//            /**
//             * 当前输出的主要方法：将数据计算结果一个个传入并输出。在这边可以自定义输出到数据库...
//             * @param record
//             * @throws IOException
//             */
//            @Override
//            public void writeRecord(Tuple2<String, Integer> record) throws IOException {
//                System.err.println(prefix + record);
//            }
//
//            @Override
//            public void close() throws IOException {
//
//            }
//        });


//        sum.writeUsingOutputFormat(new OutputFormat<Tuple2<String, Integer>>() {
//            private String prefix = null;
//            @Override
//            public void configure(Configuration parameters) {
//                //无需配置不写
//            }
//            //2> (hello,1)
//            @Override
//            public void open(int taskNumber, int numTasks) throws IOException {
//                System.err.println("taskNumber" + taskNumber + "numTasks :" +numTasks );
//                prefix = taskNumber + 1 +">";
//            }
//            //dataStream的计算结果最终都会流向这边。
//            @Override
//            public void writeRecord(Tuple2<String, Integer> record) throws IOException {
//                //因为到这边已经是有结果的，具体怎么处置随意
//                System.err.println(prefix+ record);
//            }
//            @Override
//            public void close() throws IOException {
//                //无需配置
//            }
//        });


//        sum.writeUsingOutputFormat(new OutputFormat<Tuple2<String, Integer>>() {
//            private  String prefix = null;
//
//            /**
//             * flink会传配置进来，可以用，也可以不用parameters
//             * @param parameters
//             */
//            @Override
//            public void configure(Configuration parameters) {
//
//            }
//
//            /**
//             * 初始化方法...
//             * @param taskNumber task_id
//             * @param numTasks  总共task的个数
//             * @throws IOException
//             */
//            @Override
//            public void open(int taskNumber, int numTasks) throws IOException {
//                System.err.println("taskNumber: " + taskNumber + "  . numTasks: " + numTasks);
//                prefix = taskNumber + "> ";
//            }
//
//            /**
//             * 主要方法，会把数据流的计算结果一个一个的传进来。
//             * 在这里，我们直接将结果进行打印，也可以把它存到数据库等。。
//             * @param record
//             * @throws IOException
//             */
//            @Override
//            public void writeRecord(Tuple2<String, Integer> record) throws IOException {
//                System.err.println(prefix + record );
//            }
//
//            /**
//             * 关闭流的时候，会调用此方法，一般做收尾工作，比如释放资源啥的。
//             * @throws IOException
//             */
//            @Override
//            public void close() throws IOException {
//
//            }
//        });

        try {
            //转换算子都是懒执行的，最后要显示调用 执行程序，
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
