package com.common.common.flink.batch;

import org.apache.flink.api.common.JobExecutionResult;
import org.apache.flink.api.common.accumulators.IntCounter;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
//import org.apache.log4j.lf5.util.ResourceUtils;

public class AccumulatorTest {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSet<String> ds = env.readTextFile("data/textFile");

        env.setParallelism(1);

        MapOperator<String, String> map = ds.map(new RichMapFunction<String, String>() {

            IntCounter intCounter = new IntCounter();

            //open方法是富函数的，对于runtimeContext进行相关配置
            @Override
            public void open(Configuration parameters) throws Exception {
                //拿到环境进行累加器的注册
                getRuntimeContext().addAccumulator("acc",intCounter);
            }

            //富函数本身还是带有相应算子的逻辑判断
            @Override
            public String map(String value) throws Exception {
                if(value.contains("joy")){
                    intCounter.add(1);
                }
                return value;
            }
        });

        map.writeAsText("data/acc.txt", FileSystem.WriteMode.OVERWRITE);
//
        JobExecutionResult result = env.execute("intCounter");

        Integer acc = result.getAccumulatorResult("acc");

        System.out.println(acc);

    }
}
