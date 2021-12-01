package com.common.common.flink.stream.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Random;


/**
 * 向kafka中生产数据
 *
 * @author root
 */
public class ProducerForKafka extends Thread {

    static String[] lines = new String[]{
            "Spark Scala Kafka",
            "Scala Kafka ML",
            "Flink Hadoop Storm",
            "Hive Impala HBase",
    };


    private String topic; //发送给Kafka的数据,topic
    private KafkaProducer<Integer, String> producerForKafka;


    public ProducerForKafka(String topic) {

        this.topic = topic;
        Properties conf = new Properties();
        conf.put("bootstrap.servers", "bd1701:9092,bd1702:9092,bd1703:9092");
        conf.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        conf.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerForKafka = new KafkaProducer<>(conf);
    }


    @Override
    public void run() {

        Random random = new Random();


        while (true) {

            String message = lines[random.nextInt(lines.length)];

            producerForKafka.send(new ProducerRecord<>(topic, message));

            System.out.println(message);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {

        new ProducerForKafka("flink-source").start();


    }

}

