package com.common.common.flink.stream.source_operator;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

public class Source_SocketStream {


    public static void main(String[] args) {
        //设置执行环境，类似spark中初始化sparkcontext一样
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        env.setParallelism(3);


        //获取数据源。在node01节点，监听9999端口的数据。 nc -lk 9999
        DataStreamSource<String> dataStreamSource = env.socketTextStream("node01",9999);
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

        sum.print();



        try {
            //转换算子都是懒执行的，最后要显示调用 执行程序，
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
