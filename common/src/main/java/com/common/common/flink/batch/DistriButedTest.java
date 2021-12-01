package com.common.common.flink.batch;

import org.apache.commons.io.FileUtils;
import org.apache.flink.api.common.cache.DistributedCache;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DistriButedTest {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();

        String property = System.getProperty("user.dir");
        System.out.println(property);
////        "G:\\BD16\\0802Apache Flink\\002_code\\flink1.7"
//        //本地路径"file:///some/path" or "hdfs://host:port/and/path" hdfs路径

        env.registerCachedFile("file:///"+property+"/data/textfile","cachefile");

        DataSource<String> dataSource = env.fromElements("hello", "shsxt");
        
        dataSource.map(new RichMapFunction<String, String>() {
            @Override
            public void open(Configuration parameters) throws Exception {
                File file = getRuntimeContext().getDistributedCache().getFile("cachefile");
                List<String> list = FileUtils.readLines(file);
                for(String line : list){
                    System.out.println(line);
                }
            }

            @Override
            public String map(String value) throws Exception {
                return value;
            }
        }).print();

    }
}
