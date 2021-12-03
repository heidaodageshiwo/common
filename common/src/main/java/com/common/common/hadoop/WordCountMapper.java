package com.common.common.hadoop;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.hadoop
 * @ClassName: WordCountMapper
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-12-03 23:25
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-12-03 23:25
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text,
    IntWritable>{
  Text k = new Text();
  IntWritable v = new IntWritable(1);
  @Override
  protected void map(LongWritable key, Text value, Context context)
      throws IOException, InterruptedException {
// 1 获取一行
    String line = value.toString();
// 2 切割
    String[] words = line.split(" ");
// 3 输出
    for (String word : words) {
      k.set(word);
      context.write(k, v);
    }
  }
}
