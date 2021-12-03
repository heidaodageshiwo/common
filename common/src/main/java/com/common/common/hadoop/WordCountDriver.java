package com.common.common.hadoop;
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.hadoop
 * @ClassName: WordCountDriver
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-12-03 23:26
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-12-03 23:26
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
public class WordCountDriver {

  public static void main(String[] args) throws IOException,
      ClassNotFoundException, InterruptedException {
// 1 获取配置信息以及获取 job 对象
    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf);
// 2 关联本 Driver 程序的 jar
    job.setJarByClass(WordCountDriver.class);
// 3 关联 Mapper 和 Reducer 的 jar
    job.setMapperClass(WordCountMapper.class);
    job.setReducerClass(WordCountReducer.class);
// 4 设置 Mapper 输出的 kv 类型
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
// 5 设置最终输出 kv 类型
    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);
// 6 设置输入和输出路径
  /*  FileInputFormat.setInputPaths(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));*/
    FileInputFormat.setInputPaths(job, new Path("D:\\hadoop\\zhangqiang\\input"));
    FileOutputFormat.setOutputPath(job, new Path("D:\\hadoop\\zhangqiang\\output"));
// 7 提交 job
    boolean result = job.waitForCompletion(true);
    System.exit(result ? 0 : 1);
  }
}
