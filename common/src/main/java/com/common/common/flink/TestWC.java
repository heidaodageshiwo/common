package com.common.common.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * flink读取文件进行处理，模拟批处理操作
 *
 * @author Mr.Jaden
 * @date 2020/11/5 10:28
 * @description 每天起床第一句，先给自己打个气
 */
public class TestWC {
    public static void main(String[] args) throws Exception {
        //拿到flink环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        DataSource<String> dataSource = env.readTextFile("data/textfile");

        DataSet<Tuple2<String, Integer>> wordCount = dataSource
                .flatMap(new LineToWordOne())
                .groupBy(0)//dataSet限定，dataStream是keyBy
                .sum(1);

        //数据output
        wordCount.print();


    }

    public static class LineToWordOne implements FlatMapFunction<String, Tuple2<String,Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String,Integer>> out) throws Exception {
            String[] split = value.split(" ");
            for(String word :split){
                out.collect(new Tuple2<String,Integer>(word,1));
            }
        }
    }




}


