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

/**
 * 对于kafka这种有官方支持连接器的外部系统
 * addSource
 * addSink都帮你写好了
 *
 */
public class FlinkKafka {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment executionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment();

        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers","bd1701:9092,bd1702:9092,bd1703:9092");

        properties.setProperty("group.id","mygroup");

        FlinkKafkaConsumer011<String> flinktest = new FlinkKafkaConsumer011<>("flink-source", new SimpleStringSchema(), properties);

        DataStreamSource<String> source = executionEnvironment.addSource(flinktest);

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
        //（hello,16）（nihao,22）数据格式改变
        SingleOutputStreamOperator<String> result = sum.map(new MapFunction<Tuple2<String, Integer>, String>() {
            @Override
            public String map(Tuple2<String, Integer> tuple2) throws Exception {
                System.out.println(tuple2.f0 + "#" + tuple2.f1);
                return tuple2.f0 + "#" + tuple2.f1;
            }
        });
        //将改变的数据插入到kafka
        FlinkKafkaProducer011 kafkaProducer011 = new FlinkKafkaProducer011<>("flink-result",new SimpleStringSchema(),properties);

        result.addSink(kafkaProducer011);

        executionEnvironment.execute("flink-kafka");
    }


}
