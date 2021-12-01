package com.common.common.flink.demo.cep.loginDemo2;


import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class LoginFailWarningDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        env.setParallelism(1);
        //设置用户的事件时间，作为数据流的时间
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //自定义事件匹配规则，用户在1分钟内，连续登录失败3次，则发出警告。
        Pattern<LoginEvent, LoginEvent> pattern = Pattern.<LoginEvent>begin("first").where(new IterativeCondition<LoginEvent>() {
            @Override
            public boolean filter(LoginEvent loginEvent, Context<LoginEvent> ctx) throws Exception {
                return loginEvent.getType().equals("fail");
            }
        }).next("second").where(new IterativeCondition<LoginEvent>() {
            @Override
            public boolean filter(LoginEvent loginEvent, Context<LoginEvent> ctx) throws Exception {
                return loginEvent.getType().equals("fail");
            }
        }).next("third").where(new IterativeCondition<LoginEvent>() {
            @Override
            public boolean filter(LoginEvent loginEvent, Context<LoginEvent> ctx) throws Exception {
                return loginEvent.getType().equals("fail");
            }
        }).within(Time.minutes(1));

        //加载数据流
        DataStreamSource<String> textFile = env.readTextFile("./data/cep-login.csv");

        //转换数据流
        SingleOutputStreamOperator<LoginEvent> loginEventStream = textFile.map(new MapFunction<String, LoginEvent>() {
            @Override
            public LoginEvent map(String line) throws Exception {
                String[] split = line.split(",");
                return new LoginEvent(split[0], split[1], split[2], split[3]);
            }
        });

        //抽取数据流的事件自带时间，作为时间戳。
        SingleOutputStreamOperator<LoginEvent> dataStream = loginEventStream.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<LoginEvent>() {
            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

            @Override
            public long extractAscendingTimestamp(LoginEvent element) {
                Date date = null;
                try {
                    date = format.parse(element.getTimestap());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return date.getTime();
            }
        });

        //对数据流，按用户ID进行分组

        KeyedStream<LoginEvent, Tuple> cepInputStream = dataStream.keyBy("userId");

        //对事件数据流进行检测
        PatternStream<LoginEvent> patternStream = CEP.pattern(cepInputStream, pattern);

        //对检测到的事件进行操作，可以是发出警告，拉入黑名单等等..
        SingleOutputStreamOperator<LoginWarning> loginFailDataStream = patternStream.select(new PatternSelectFunction<LoginEvent, LoginWarning>() {
            @Override
            public LoginWarning select(Map<String, List<LoginEvent>> pattern) throws Exception {
                List<LoginEvent> third = pattern.get("third");
                LoginEvent loginEvent = third.get(0);
                return new LoginWarning(loginEvent.getUserId(),loginEvent.getType(), loginEvent.getIp(), loginEvent.getTimestap());
            }
        });


        loginFailDataStream.printToErr();

        env.execute();
    }
}
