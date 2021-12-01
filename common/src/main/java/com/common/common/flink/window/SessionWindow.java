package com.common.common.flink.window;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.ProcessingTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

import java.text.SimpleDateFormat;
import java.util.Random;

/**
 * 会话窗口：窗口不可重叠
 */
public class SessionWindow {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);

        DataStreamSource<String> dataStreamSource = env.socketTextStream("bd1701", 9999);

        SingleOutputStreamOperator<Tuple2<String, Integer>> map = dataStreamSource.map(new MapFunction<String, Tuple2<String, Integer>>() {
            /**
             * map逻辑如下，将传入的数据直接打印，配上一个当前逻辑执行的时间和一个随机数标识
             * @param line
             * @return
             * @throws Exception
             */
            @Override
            public Tuple2<String, Integer> map(String line) throws Exception {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                long timeMillis = System.currentTimeMillis();
                int random = new Random().nextInt(10);
                System.err.println("value:" + line + " random :" + random + " timMillis :" + timeMillis +"format : " + simpleDateFormat.format(timeMillis));
                return new Tuple2<>(line, random);
            }
        });

        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = map.keyBy(0);
        //如果连续10s没有数据进来，那么会话窗口会断开，等待新数据进来创建新的。 processingTime处理事件的间隔作为判断条件
        WindowedStream<Tuple2<String, Integer>, Tuple, TimeWindow> window = keyedStream.window(ProcessingTimeSessionWindows.withGap(Time.seconds(10)));

        window.sum(1).print();

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
