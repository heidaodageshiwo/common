/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.common.common.flink.demo.topGoods;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.WindowedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;

import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import scala.Int;

import javax.annotation.Nullable;
import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;

public class HotItems {

	public static void main(String[] args) throws Exception {

		// 创建 execution environment
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		// 告诉系统按照 EventTime 处理
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		// 为了打印到控制台的结果不乱序，我们配置全局的并发为1，改变并发对结果正确性没有影响
		env.setParallelism(1);

        DataStreamSource<String> textFile = env.readTextFile("data/UserBehavior1.csv");

        // 创建数据源，得到 UserBehavior 类型的 流
        SingleOutputStreamOperator<UserBehavior> ds = textFile.map(new MapFunction<String, UserBehavior>() {
            @Override
            public UserBehavior map(String value) throws Exception {
                String[] split = value.split(",");
                long userID = Long.valueOf(split[0]);
                long itemID = Long.valueOf(split[1]);
                int categoryId = Integer.valueOf(split[2]);
                String behavior = split[3];
                long timestap = Long.valueOf(split[4]);
                return new UserBehavior(userID, itemID, categoryId, behavior, timestap);
            }
        });


        // 抽取出时间和生成 watermark，水位线随时间而递增，即水位线和当前流中数据最大时间相等。
        SingleOutputStreamOperator<UserBehavior> outputStreamOperator = ds.assignTimestampsAndWatermarks(new AscendingTimestampExtractor<UserBehavior>() {
            @Override
            public long extractAscendingTimestamp(UserBehavior userBehavior) {
                // 原始数据的时间，作为水位线
                return userBehavior.timestamp ;
            }
        });

        // 过滤出只有点击的数据
        SingleOutputStreamOperator<UserBehavior> filterOutputStream = outputStreamOperator.filter(new FilterFunction<UserBehavior>() {
            @Override
            public boolean filter(UserBehavior userBehavior) throws Exception {
                // 过滤出只有点击的数据
                return userBehavior.behavior.equals("pv");
            }
        });

        //按商品ID进行分组
        KeyedStream<UserBehavior, Tuple> keyedStream = filterOutputStream.keyBy("itemId");

        //每5分钟计算一下最近60分钟的数据
        WindowedStream<UserBehavior, Tuple, TimeWindow> windowedStream = keyedStream.timeWindow(Time.minutes(60) ,Time.minutes(5));

        //进行聚合计算.统计出每个商品的点击次数
        SingleOutputStreamOperator<ItemViewCount> apply = windowedStream.apply(new WindowFunction<UserBehavior, ItemViewCount, Tuple, TimeWindow>() {
            @Override
            public void apply(Tuple key, TimeWindow window, Iterable<UserBehavior> input, Collector<ItemViewCount> out) throws Exception {
                Long itemId = key.getField(0);
                long sum = 0;
                //统计每个商品ID点击的次数。
                Iterator<UserBehavior> iterator = input.iterator();
                while (iterator.hasNext()) {
                    sum++;
                    iterator.next();
                }
                out.collect(ItemViewCount.of(itemId, window.getEnd(), sum));
            }
        });

        KeyedStream<ItemViewCount, Tuple> windowEnd = apply.keyBy("windowEnd");
//
        SingleOutputStreamOperator<String> process = windowEnd.process(new TopNHotItems(3));
////
        process.print();

        env.execute("Hot Items Job");
	}

}
