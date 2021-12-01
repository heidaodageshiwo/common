package com.common.common.flink.apitest.sink;/**
 * Copyright (c) 2018-2028 尚硅谷 All Rights Reserved
 * <p>
 * Project: FlinkTutorial Package: com.atguigu.apitest.sink Version: 1.0
 * <p>
 * Created by wushengran on 2020/11/9 11:21
 */

import com.common.common.flink.apitest.beans.SensorReading;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkBase;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink.Builder;
import org.apache.flink.streaming.connectors.elasticsearch6.RestClientFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.util.ArrayList;
import java.util.HashMap;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;

public class SinkTest3_Es {

  public static void main(String[] args) throws Exception {
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
    env.setParallelism(1);

    // 从文件读取数据
    DataStream<String> inputStream = env.readTextFile("D:\\sensor.txt");

    // 转换成SensorReading类型
    DataStream<SensorReading> dataStream = inputStream.map(line -> {
      String[] fields = line.split(",");
      return new SensorReading(fields[0], new Long(fields[1]), new Double(fields[2]));
    });

    // 定义es的连接配置  不带用户名密码
    ArrayList<HttpHost> httpHosts = new ArrayList<>();
    httpHosts.add(new HttpHost("localhost", 9200));
    dataStream.addSink(
        new ElasticsearchSink.Builder<SensorReading>(httpHosts, new MyEsSinkFunction()).build());
    env.execute();

    // 定义es的连接配置  带用户名密码
   /* RestClientFactory restClientFactory = new RestClientFactory() {

      @Override
      public void configureRestClientBuilder(RestClientBuilder restClientBuilder) {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
            new UsernamePasswordCredentials("用户名", "密码"));
        restClientBuilder.setHttpClientConfigCallback(new HttpClientConfigCallback() {
          @Override
          public HttpAsyncClientBuilder customizeHttpClient(
              HttpAsyncClientBuilder httpAsyncClientBuilder) {
            httpAsyncClientBuilder.disableAuthCaching();
            return httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
          }
        });
      }
    };

    ArrayList<HttpHost> httpHosts = new ArrayList<>();
    httpHosts.add(new HttpHost("localhost", 9200));
    ElasticsearchSink.Builder<SensorReading> sensorReadingBuilder = new ElasticsearchSink.Builder<>(
        httpHosts,
        new MyEsSinkFunction());
    sensorReadingBuilder.setRestClientFactory(restClientFactory);
    dataStream.addSink(sensorReadingBuilder.build());
    env.execute();*/
  }

  // 实现自定义的ES写入操作
  public static class MyEsSinkFunction implements ElasticsearchSinkFunction<SensorReading> {

    @Override
    public void process(SensorReading element, RuntimeContext ctx, RequestIndexer indexer) {
      // 定义写入的数据source
      HashMap<String, String> dataSource = new HashMap<>();
      dataSource.put("id", element.getId());
      dataSource.put("temp", element.getTemperature().toString());
      dataSource.put("ts", element.getTimestamp().toString());

      // 创建请求，作为向es发起的写入命令
      IndexRequest indexRequest = Requests.indexRequest()
          .index("sensor")
          .type("readingdata")
          .source(dataSource);

      // 用index发送请求
      indexer.add(indexRequest);
    }
  }
}
