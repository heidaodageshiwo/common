package com.common.common.flink.window;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;

public class MyTimeWindowFunction implements WindowFunction<Tuple2<String,Integer>,String, Tuple, TimeWindow> {
    /**
     * 模拟sum功能
     * @param tuple
     * @param window
     * @param input
     * @param out
     * @throws Exception
     */
    @Override
    public void apply(Tuple tuple, TimeWindow window, Iterable<Tuple2<String, Integer>> input, Collector<String> out) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        int sum = 0;
        for(Tuple2<String, Integer> tuple2:input){
            sum += tuple2.f1;
        }
        //对于timeWindow而言起止时间是重要的参数
        long start = window.getStart();
        long end = window.getEnd();


//        out.collect(sum+"");
        //自定义复杂输出结果，包含具体时间和内容
        out.collect("key:"+tuple.getField(0)+" value:"+sum + " window start :"+format.format(start) +
                " window end :"+format.format(end));
    }

}
