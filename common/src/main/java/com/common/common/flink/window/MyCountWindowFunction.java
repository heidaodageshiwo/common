package com.common.common.flink.window;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;

public class MyCountWindowFunction implements WindowFunction<Tuple2<String, Integer>, String,Tuple, GlobalWindow> {

    @Override
    public void apply(Tuple tuple, GlobalWindow window, Iterable<Tuple2<String, Integer>> input, Collector<String> out) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int sum = 0;
        for(Tuple2<String, Integer> tuple2:input){
            sum += tuple2.f1;
        }
        //对于基于事件驱动的计数窗口而言，时间戳是无用的，默认Long.MAX_VALUE
        long maxTimestamp = window.maxTimestamp();


//        out.collect(sum+"");
        //自定义复杂输出结果，包含具体时间和内容
        out.collect("key:"+tuple.getField(0)+" value:"+sum +" maxTimeStamp:" +maxTimestamp +","+format.format(maxTimestamp));
    }
}
