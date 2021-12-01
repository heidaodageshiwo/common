package com.common.common.flink.stream.kafka;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.util.Collector;

import java.util.Properties;

public class FlinkKafkaProperties {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        executionEnvironment.enableCheckpointing(5000);

        Properties properties = new Properties();
//        properties.setProperty("bootstrap.servers","node01:9092,node02:9092,node03:9092");
        properties.setProperty("bootstrap.servers","bd1701:9092,bd1702:9092,bd1703:9092");
        properties.setProperty("group.id","mygroup");
        properties.setProperty("auto.offset.reset","earliest");
        properties.setProperty("enable.auto.commit","false");
        /**
         * 第一个参数是topic
         * 第二个参数是反序列化格式
         * 第三个参数是kafka配置
         */
        FlinkKafkaConsumer011<String> kafkaConsumer011 = new FlinkKafkaConsumer011<>("flink-source", new SimpleStringSchema(), properties);
//        kafkaConsumer011.setStartFromEarliest();//从topic的最早偏移量开始消费
        kafkaConsumer011.setStartFromLatest();//从topic的最新偏移量开始消费
//        kafkaConsumer011.setStartFromGroupOffsets();//默认方式，从kafka中找到消费者组对应的偏移量开始消费，若没有则按照auto.offset.reset的配置策略
//        kafkaConsumer011.setStartFromTimestamp(1111);//从指定的时间戳开始消费
        //开启checkpoint保存状态---包含offset，将checkpoint中的offset提交到kafka中。保证内外offset一直。
        kafkaConsumer011.setCommitOffsetsOnCheckpoints(true);

        DataStreamSource<String> source = executionEnvironment.addSource(kafkaConsumer011);

        SingleOutputStreamOperator<Tuple2<String, Integer>> flatMap = source.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String line, Collector<Tuple2<String, Integer>> collector) throws Exception {
                String[] split = line.split(" ");
                for (String word : split) {
                    collector.collect(new Tuple2<>(word, 1));
                }
            }
        });

        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = flatMap.keyBy(0);

        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);

        FlinkKafkaProducer011 kafkaProducer011 = new FlinkKafkaProducer011<>("flink-result2",new SimpleStringSchema(),properties);

        SingleOutputStreamOperator<String> result = sum.map(new MapFunction<Tuple2<String, Integer>, String>() {
            @Override
            public String map(Tuple2<String, Integer> tuple2) throws Exception {
                System.out.println(tuple2.f0 + "#" + tuple2.f1);
                return tuple2.f0 + "#" + tuple2.f1;
            }
        });



        result.addSink(kafkaProducer011);

        executionEnvironment.execute("flink-kafka");






    }


}
