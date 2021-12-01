package com.common.common.flink.stream.test;

import org.apache.flink.api.common.JobID;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.typeutils.TypeSerializer;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;

import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.metrics.MetricGroup;
import org.apache.flink.runtime.execution.Environment;
import org.apache.flink.runtime.query.TaskKvStateRegistry;
import org.apache.flink.runtime.state.*;

import org.apache.flink.runtime.state.filesystem.AbstractFileStateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.runtime.state.memory.MemoryStateBackend;
import org.apache.flink.runtime.state.ttl.TtlTimeProvider;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.rocksdb.RocksDB;

import java.io.IOException;


public class CheckPointTest {

    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //启动checkpoint,并且设置多久进行一次checkpoint,即两次checkpoint的时间间隔.
        env.enableCheckpointing(5000);

        env.setParallelism(1);

        CheckpointConfig checkpointConfig = env.getCheckpointConfig();
//
//        env.setRestartStrategy(RestartStrategies.fallBackRestart());

//        //设置checkpoint的语义，一般使用 exactly_once 语义。at_least_once 一般在那些非常低的延迟场景使用。
        checkpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        checkpointConfig.setCheckpointInterval();
//        //设置检查点之间的最短时间
//        //检查点之间的最短时间：为确保流应用程序在检查点之间取得一定进展，可以定义检查点之间需要经过多长时间。
//        //如果将此值设置为例如500，则无论检查点持续时间和检查点间隔如何，下一个检查点将在上一个检查点完成后的500ms内启动
//        //请注意，这意味着检查点间隔永远不会小于此参数。
        checkpointConfig.setMinPauseBetweenCheckpoints(500);
        // 同一时间最多可以进行多少个checkpoint
//        // 默认情况下，当一个检查点仍处于运行状态时，系统不会触发另一个检查点
        checkpointConfig.setMaxConcurrentCheckpoints(1);
//
//        // 设置超时时间,若本次的checkpoint时间超时,则放弃本次checkpoint操作
        checkpointConfig.setCheckpointTimeout(60000);

        //checkpoint本身计算需要消耗性能，先算ck再算结果。所以合理设置ck之间的时间保证计算的正常运行，并且尽量少开启ck计算，避免结果计算问题。
        //---------------------------------------------------------------------------------------------

//
//
//
//        //开启checkpoints的外部持久化，但是在job失败的时候不会自动清理，需要自己手工清理state
//        //DELETE_ON_CANCELLATION:在job canceled的时候会自动删除外部的状态数据，但是如果是FAILED的状态则会保留；
//        //RETAIN_ON_CANCELLATION:在job canceled的时候会保留状态数据
//        checkpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
//
//        //默认使用内存的方式存储状态值。单次快照的状态上限内存为10MB，使用同步方式进行快照。

//        env.setStateBackend(new MemoryStateBackend(10*1024*1024,true));
//        //使用FsStateBackend的方式进行存储。并且是同步方式进行快照。
//        env.setStateBackend(new FsStateBackend("hdfs://bd1701:8020/data/flink-checkpoint1",false));
//
        try {
            env.setStateBackend(new RocksDBStateBackend("hdfs://bd1701:8020/data/flink-3",true));
        } catch (IOException e) {
            e.printStackTrace();
        }


        DataStreamSource<String> dataStreamSource = env.socketTextStream("bd1701",9999);

        SingleOutputStreamOperator<Tuple2<String, Integer>> pairStream = dataStreamSource.flatMap(new FlatMapFunction<String, Tuple2<String, Integer>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
                String[] split = value.split(" ");
                for (String word : split) {

                    out.collect(new Tuple2<>(word, 1));
                }
            }
        });


        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = pairStream.keyBy(0);


        SingleOutputStreamOperator<Tuple2<String, Integer>> sum = keyedStream.sum(1);

//
        sum.print();


        try {
            //转换算子都是懒执行的，最后要显示调用 执行程序，
            env.execute("checkpoint-test");
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
