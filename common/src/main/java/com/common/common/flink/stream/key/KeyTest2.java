package com.common.common.flink.stream.key;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.datastream.*;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * 使用字段表达式来指定虚拟的key，Field Expression
 *
 * 姓名,性别,age,年级,三门成绩
 *
 * hl m 20 4 99 100 100
 * wz m 18 3 0 0 0
 * jrw m 20 4 99 99 99
 * joy f 18 6 60 60 60
 */
public class KeyTest2 {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStreamSource<String> ds = env.socketTextStream("bd1701", 9999);

        SingleOutputStreamOperator<StudentInfo> map = ds.map(new MapFunction<String, StudentInfo>() {
            @Override
            public StudentInfo map(String value) throws Exception {
                String[] split = value.split(" ");
                return new StudentInfo(split[0], split[1], Integer.parseInt(split[2]),
                        new Tuple2<>(Integer.parseInt(split[3]),
                                new Tuple3<>(Integer.parseInt(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6]))));
            }
        });

        //对于对象而言可以通过字段表达式进行虚拟key的指定
//        KeyedStream<StudentInfo, Tuple> keyBy = map.keyBy("name");
//        KeyedStream<StudentInfo, Tuple> keyBy = map.keyBy("gender");
        KeyedStream<StudentInfo, Tuple> keyBy = map.keyBy("gradeAndScore.f0");
//        KeyedStream<StudentInfo, Tuple> keyBy = map.keyBy("gradeAndScore.f1.f0");

        keyBy.timeWindow(Time.seconds(5)).reduce(new ReduceFunction<StudentInfo>() {
            @Override
            public StudentInfo reduce(StudentInfo value1, StudentInfo value2) throws Exception {
                String name = value1.getName() + "?" + value2.getName();
                String gender = value1.getGender() + "!" + value2.getGender();
                Integer age = value1.getAge() + value2.getAge();

                Tuple2<Integer, Tuple3<Integer, Integer, Integer>> tuple1 = value1.getGradeAndScore();
                Tuple2<Integer, Tuple3<Integer, Integer, Integer>> tuple2 = value2.getGradeAndScore();

                Tuple2<Integer, Tuple3<Integer, Integer, Integer>> tuple3 = new Tuple2<>(
                        tuple1.f0 + tuple2.f0, new Tuple3<>(
                        tuple1.f1.f0 + tuple2.f1.f0,
                        tuple1.f1.f1 + tuple2.f1.f1,
                        tuple1.f1.f2 + tuple2.f1.f2
                ));

                return new StudentInfo(name,gender,age,tuple3);
            }
        }).print();

        try {
            env.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
