package com.common.common.flink.batch;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

public class MyWordCount {

    public static void main(String[] args) throws Exception {


        ExecutionEnvironment environment = ExecutionEnvironment.getExecutionEnvironment();

        DataSet<String> textfile = environment.readTextFile("data/textfile");

        textfile.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");

                for (String word : split) {
                    out.collect(new Tuple2<>(word, 1));
                }

            }
        }).groupBy(0).sum(1).print();




    }
}
