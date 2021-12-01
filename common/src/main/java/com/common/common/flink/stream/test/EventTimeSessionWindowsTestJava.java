package com.common.common.flink.stream.test;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;
import java.text.SimpleDateFormat;


/**
 * @author lynn
 */
public class EventTimeSessionWindowsTestJava {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //设置自动生成wms时间 默认200ms
        env.getConfig().setAutoWatermarkInterval(7000);

        DataStreamSource<String> inputStream = env.socketTextStream("node01", 9999);

        SingleOutputStreamOperator<Tuple2<String, Long>> dataStream = inputStream
                .map(new MapFunction<String, Tuple2<String, Long>>() {
                    @Override
                    public Tuple2<String, Long> map(String value) throws Exception {
                        String[] str = value.split(" ");
                        return new Tuple2<String, Long>(str[0], Long.parseLong(str[1]));
                    }
                })
                .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple2<String, Long>>() {
                    //设置延迟时间1s
                    Long bound = 1000L;
                    //过来数据的最大时间戳
                    Long maxTs = 0l;
                    Watermark wm=  null;
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

                    @Nullable
                    @Override
                    public Watermark getCurrentWatermark() {
                        wm = new Watermark(maxTs - bound);
                        System.out.println(format.format(System.currentTimeMillis()) + " 获取当前水位线: " + wm );
                        return wm;
                    }

                    @Override
                    public long extractTimestamp(Tuple2<String, Long> element, long previousElementTimestamp) {
                        long timestamp = element.f1 ;
                        maxTs = Math.max(maxTs, timestamp * 1000L);
                        System.err.println(element.f0 +", 本条数据的时间戳: "+ timestamp + "," +format.format(timestamp* 1000L)
                                + "|目前数据中的最大时间戳: "+  maxTs + ","+ format.format(maxTs)
                                + "|水位线时间戳: "+ wm + ","+ format.format(wm.getTimestamp()*1000));
                        return timestamp * 1000L;
                    }
                })
                .keyBy(0)
                .timeWindow(Time.seconds(10))
                .sum(1);
        dataStream.print();

        env.execute();


    }
}

