package com.common.common.flink.batch;

import org.apache.flink.api.common.functions.RichFilterFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FilterOperator;
import org.apache.flink.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;

public class BroadCastTest {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> textFile = env.readTextFile("data/textFile");

        List<String> list = new ArrayList<>();

        list.add("shsxt");
        list.add("bjsxt");

        DataSource<String> dataset = env.fromCollection(list);

        FilterOperator<String> f1 = textFile.filter(new RichFilterFunction<String>() {
            List<String> whiteList = null;

            @Override
            public boolean filter(String value) throws Exception {
                for (String i : whiteList) {
                    if (value.contains(i)) {
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void open(Configuration parameters) throws Exception {
                whiteList = getRuntimeContext().getBroadcastVariable("white-list");
            }
        });
        FilterOperator<String> f2 = f1.withBroadcastSet(dataset, "white-list");
        f2.print();
    }

}
