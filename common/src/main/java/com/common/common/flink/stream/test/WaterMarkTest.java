package com.common.common.flink.stream.test;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * hello,2019-09-17 11:34:05.890
 * hello,2019-09-17 11:34:07.890
 * hello,2019-09-17 11:34:13.890
 * hello,2019-09-17 11:34:08.890
 * hello,2019-09-17 11:34:16.890
 * hello,2019-09-17 11:34:19.890
 * hello,2019-09-17 11:34:21.890
 */
public class WaterMarkTest {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //设置时间类型
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);//解决乱序，延迟高
//        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);//杂乱无章，延迟低
//        env.setStreamTimeCharacteristic(TimeCharacteristic.IngestionTime);//遗忘了

        //设置多久查看水位线，默认200ms
        System.out.println(env.getConfig().getAutoWatermarkInterval());
//        env.getConfig().setAutoWatermarkInterval(5000);
        env.getConfig().setAutoWatermarkInterval(10000);

        DataStreamSource<String> stream = env.socketTextStream("localhost", 9999);

        //水印就在流中，作为一个时间戳存在
        stream.assignTimestampsAndWatermarks(new MyWaterMark())
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        String[] split = value.split(",");

                        return new Tuple2<>(split[0], 1);
                    }
                })
                .keyBy(0)
                .timeWindow(Time.seconds(10))
                .apply(new MyWindowFuntion())
                .printToErr();


        try {
            env.execute("wmtest");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

class MyWindowFuntion implements WindowFunction<Tuple2<String, Integer>, String, Tuple, TimeWindow> {
    @Override
    public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple2<String, Integer>> input, Collector<String> out) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int sum = 0;
        for (Tuple2<String, Integer> tuple2 : input) {
            sum += tuple2.f1;
        }
        long start = window.getStart();
        long end = window.getEnd();

        out.collect("key:" + tuple.getField(0) +
                " value:" + sum +
                " window_start:" + simpleDateFormat.format(start) +
                " window_end:" + simpleDateFormat.format(end)
        );

    }
}

class MyWaterMark implements AssignerWithPeriodicWatermarks<String> {

    //水位线本身是一个时间戳

    long currentMaxTimestamp = 0;
    //根据窗口以及业务决定容忍度,当前允许延迟5s
    long maxLateTime = 5000;
    Watermark waterMark = null;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    @Nullable
    @Override
    public Watermark getCurrentWatermark() {
        waterMark = new Watermark(currentMaxTimestamp - maxLateTime);
        System.out.println(
                simpleDateFormat.format(System.currentTimeMillis()) + " 当前水位线：" +
                        waterMark + simpleDateFormat.format(waterMark.getTimestamp())
        );
        return waterMark;
    }


    //hello,2019-09-17 11:34:05.890
    @Override
    public long extractTimestamp(String element, long previousElementTimestamp) {
        String[] split = element.split(",");
        String key = split[0];
        long timestamp = 0;
        try {
            timestamp = simpleDateFormat.parse(split[1]).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        currentMaxTimestamp = Math.max(timestamp, currentMaxTimestamp);

        System.err.println("key:" + key + "，该条消息时间戳为：" + timestamp + "," + simpleDateFormat.format(timestamp) +
                "，当前数据最大的数据戳：" + currentMaxTimestamp + "," + simpleDateFormat.format(currentMaxTimestamp) +
                ",水位线为：" + waterMark + "," + simpleDateFormat.format(waterMark.getTimestamp()));
        return timestamp;
    }
}
