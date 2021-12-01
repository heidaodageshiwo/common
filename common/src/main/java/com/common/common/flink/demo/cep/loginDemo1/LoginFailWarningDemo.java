package com.common.common.flink.demo.cep.loginDemo1;

import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 整个模式匹配的规则在5秒内，如果连续两次登录失败，则发出警告。。
 */
public class LoginFailWarningDemo {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //1，流创建
        DataStream<LoginEvent> loginEventStream = env.fromCollection(Arrays.asList(
                new LoginEvent("1", "192.168.0.1", "fail"),
                new LoginEvent("1", "192.168.0.2", "fail"),
                new LoginEvent("1", "192.168.0.3", "fail"),
                new LoginEvent("2", "192.168.10.10", "fail"),
                new LoginEvent("2", "192.168.10.10", "success")
        ));
        KeyedStream<LoginEvent, Tuple> userId = loginEventStream.keyBy("userId");

        //模式匹配begin开始，where（表达式），next表示事件序列（紧邻）
        Pattern<LoginEvent, LoginEvent> pattern = Pattern.<LoginEvent>begin("begin").where(new IterativeCondition<LoginEvent>() {
            @Override
            public boolean filter(LoginEvent value, Context<LoginEvent> ctx) throws Exception {
                return value.getType().equals("fail");
            }
        }).next("next1").where(new IterativeCondition<LoginEvent>() {
            @Override
            public boolean filter(LoginEvent value, Context<LoginEvent> ctx) throws Exception {
                return value.getType().equals("fail");
            }
            //定义事件序列的匹配最大时间间隔
        }).within(Time.seconds(5));

        //CEP复杂事件匹配获取检测流
        PatternStream<LoginEvent> patternStream = CEP.pattern(userId, pattern);

        patternStream.select(new PatternSelectFunction<LoginEvent, LoginWarning>() {
            @Override
            public LoginWarning select(Map<String, List<LoginEvent>> pattern) throws Exception {
                List<LoginEvent> begin = pattern.get("begin");
                System.out.println("begin!!!!!!!!!!!!!");
                for(LoginEvent loginEvent : begin){
                    System.out.println(loginEvent);
                }

                //检测出的错误对象
                List<LoginEvent> next1 = pattern.get("next1");
                System.out.println("next1!!!!!!!!!!!!!");
                for(LoginEvent loginEvent : next1){
                    System.out.println(loginEvent);
                }
                LoginEvent loginEvent = next1.get(0);
                return new LoginWarning(loginEvent.getUserId(),loginEvent.getType(),loginEvent.getIp());
            }
        }).printToErr();

        env.execute();
    }
}
