package com.common.common.flink.window;

import org.apache.flink.api.common.ExecutionConfig;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.WindowAssigner;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.triggers.Trigger;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Random;

/**
 * 翻滚窗口
 * 1.窗口不可重叠
 * 2.需要一个windowsize
 * 两种实现方式
 * 1.基于时间驱动
 * 2.基于事件驱动
 */
public class TumblingWindow {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);
        //更改环境中的时间类型 （三种：事件，摄入，处理）
        //eventTime-------乱序数据流进行顺序处理//必须指定did you forget to call 'DataStream.assignTimestampsAndWatermarks(...)'?
//        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> dataStreamSource = env.socketTextStream("bd1701", 999);

        SingleOutputStreamOperator<Tuple2<String, Integer>> map = dataStreamSource
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                        long currentTimeMillis = System.currentTimeMillis();
                        System.out.println("line:" + value + "time:" + simpleDateFormat.format(currentTimeMillis));
                        return new Tuple2<>(value, 1);
                    }
                });

        KeyedStream<Tuple2<String, Integer>, Tuple> keyBy = map.keyBy(0);

        WindowedStream<Tuple2<String, Integer>, Tuple, TimeWindow> timeWindow = keyBy.timeWindow(Time.seconds(3));
//        WindowedStream<Tuple2<String, Integer>, Tuple, GlobalWindow> countWindow = keyBy.countWindow(3);

//        keyBy.window(TumblingProcessingTimeWindows.of(Time.seconds(5)));


//        timeWindow.sum(1).print();
        timeWindow.apply(new WindowFunction<Tuple2<String, Integer>, String, Tuple, TimeWindow>() {
            @Override
            public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple2<String, Integer>> input, Collector<String> out) throws Exception {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                int sum = 0;
                for (Tuple2<String, Integer> tuple2 : input) {
                    sum += tuple2.f1;
                }
                long start = window.getStart();
                long end = window.getEnd();
                out.collect("key:" + tuple.getField(0) + " value :" + sum +
                        " 窗口的开始时间" + simpleDateFormat.format(start) +
                        " 窗口的结束时间" + simpleDateFormat.format(end));
            }
        }).print();

//        countWindow.apply(new WindowFunction<Tuple2<String, Integer>, String, Tuple, GlobalWindow>() {
//            @Override
//            public void apply(Tuple tuple, GlobalWindow window, Iterable<Tuple2<String, Integer>> input, Collector<String> out) throws Exception {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//                int sum = 0;
//                for (Tuple2<String, Integer> tuple2 : input) {
//                    sum+= tuple2.f1;
//                }
//                long maxTimestamp = window.maxTimestamp();
//                //自定义复杂输出结果
//                out.collect("key:" + tuple.getField(0) + " value :" + sum +"maxTimestamp" + simpleDateFormat.format(maxTimestamp));
////
//            }
//        }).print();

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
