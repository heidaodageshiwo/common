package com.common.common.hadoop;
import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.hadoop
 * @ClassName: WordCountReducer
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-12-03 23:26
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-12-03 23:26
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
public class WordCountReducer extends Reducer<Text, IntWritable, Text,
    IntWritable>{
  int sum;
  IntWritable v = new IntWritable();
  @Override
  protected void reduce(Text key, Iterable<IntWritable> values,Context
      context) throws IOException, InterruptedException {
// 1 累加求和
    sum = 0;
    for (IntWritable count : values) {
      sum += count.get();
    }
// 2 输出
    v.set(sum);
    context.write(key,v);
  }
}
