package com.common.common.flink.batch;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * batch操作，对象是DataSet
 */
public class WordCount {
    public static void main(String[] args) {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //通过读取文件获取数据源(有界)
        DataSet<String> textFile = env.readTextFile("data/textfile");
        DataSet<Tuple2<String, Integer>> sum = textFile
                .flatMap(new LineSplitter())
                .groupBy(0)
                .sum(1);
        try {
            sum.print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class LineSplitter implements FlatMapFunction<String, Tuple2<String,Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            for(String word:value.split(" ")){
                out.collect(new Tuple2<>(word,1));
            }
        }
    }
}
