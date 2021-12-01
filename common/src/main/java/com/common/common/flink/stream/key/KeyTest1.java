package com.common.common.flink.stream.key;


import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * 方法1：通过tuples指定虚拟key
 * tuple是flink包下的类
 *
 * 模拟数据如下：
 * hello shanghai 1
 * hello beijing 2
 * hi guangzhou 3
 * hi shenzhen 4
 *
 */
public class KeyTest1 {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<String> ds = env.socketTextStream("bd1701", 9999);

        DataStream<Tuple3<String, String, Integer>> map = ds.map(new MapFunction<String, Tuple3<String, String, Integer>>() {
            @Override
            public Tuple3<String, String, Integer> map(String value) throws Exception {
                String[] split = value.split(" ");
                return new Tuple3<>(split[0], split[1], Integer.parseInt(split[2]));

            }
        });
        //指定流中tuple的第一个元素作为key
//        KeyedStream<Tuple3<String, String, Integer>, Tuple> keyedStream = map.keyBy(0);
        KeyedStream<Tuple3<String, String, Integer>, Tuple> keyedStream = map.keyBy(0,1);


        keyedStream.timeWindow(Time.seconds(5)).reduce(new ReduceFunction<Tuple3<String, String, Integer>>() {
            @Override
            public Tuple3<String, String, Integer> reduce(Tuple3<String, String, Integer> value1, Tuple3<String, String, Integer> value2) throws Exception {
                return new Tuple3<>(value1.f0+"?"+value2.f0,value1.f1+"!" +value2.f1,value1.f2+value2.f2);
            }
        }).print();

        try {
            env.execute("Tuple---key");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
