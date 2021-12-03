package com.common.common.hadoop;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.hadoop
 * @ClassName: HdfsClient
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-12-03 22:54
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-12-03 22:54
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
public class HdfsClient {

  //创建 HdfsClient 类 创建文件
  @Test
  public void testMkdirs() throws IOException, URISyntaxException,
      InterruptedException {
    // 1 获取文件系统
    Configuration configuration = new Configuration();
//     FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"), configuration);
    FileSystem fs = FileSystem.get(new URI("hdfs://hadoop100:8020"), configuration, "root");
    // 2 创建目录
    fs.mkdirs(new Path("/input1"));
    // 3 关闭资源
    fs.close();
  }

  //HDFS 文件上传（测试参数优先级）
  @Test
  public void testCopyFromLocalFile() throws IOException,
      InterruptedException, URISyntaxException {
    // 1 获取文件系统
    Configuration configuration = new Configuration();
    configuration.set("dfs.replication", "2");
    FileSystem fs = FileSystem.get(new URI("hdfs://hadoop100:8020"), configuration, "root");
    // 2 上传文件
    fs.copyFromLocalFile(new Path("C:\\Users\\Think\\Desktop\\配置文件\\HOSTS"), new Path("/input1"));
    // 3 关闭资源
    fs.close();
  }

  //HDFS 文件下载
  @Test
  public void testCopyToLocalFile() throws IOException,
      InterruptedException, URISyntaxException{
    // 1 获取文件系统
    Configuration configuration = new Configuration();
    FileSystem fs = FileSystem.get(new URI("hdfs://hadoop100:8020"),
        configuration, "root");

    // 2 执行下载操作
    // boolean delSrc 指是否将原文件删除
    // Path src 指要下载的文件路径
    // Path dst 指将文件下载到的路径
    // boolean useRawLocalFileSystem 是否开启文件校验
    fs.copyToLocalFile(false, new
            Path("/input1/HOSTS"), new Path("C:\\Users\\Think\\Desktop\\Seata\\HOSTS"),
        false);

    // 3 关闭资源
    fs.close();
  }

  //HDFS 文件更名和移动
  @Test
  public void testRename() throws IOException, InterruptedException,
      URISyntaxException{
// 1 获取文件系统
    Configuration configuration = new Configuration();
    FileSystem fs = FileSystem.get(new URI("hdfs://hadoop100:8020"),
        configuration, "root");
// 2 修改文件名称
    fs.rename(new Path("/input1/HOSTS"), new
        Path("/input1/HOSTS123"));
// 3 关闭资源
    fs.close();
  }



  //HDFS 删除文件和目录
  @Test
  public void testDelete() throws IOException, InterruptedException,
      URISyntaxException{
// 1 获取文件系统
    Configuration configuration = new Configuration();
    FileSystem fs = FileSystem.get(new URI("hdfs://hadoop102:8020"),
        configuration, "root");
// 2 执行删除
    fs.delete(new Path("/xiyou"), true);
// 3 关闭资源
    fs.close();
  }
//  HDFS 文件详情查看
@Test
public void testListFiles() throws IOException, InterruptedException,
    URISyntaxException {
// 1 获取文件系统
  Configuration configuration = new Configuration();
  FileSystem fs = FileSystem.get(new URI("hdfs://hadoop100:8020"),
      configuration, "root");
// 2 获取文件详情
  RemoteIterator<LocatedFileStatus> listFiles = fs.listFiles(new Path("/"),
      true);
  while (listFiles.hasNext()) {
    LocatedFileStatus fileStatus = listFiles.next();
    System.out.println("========" + fileStatus.getPath() + "=========");
    System.out.println(fileStatus.getPermission());
    System.out.println(fileStatus.getOwner());
    System.out.println(fileStatus.getGroup());
    System.out.println(fileStatus.getLen());
    System.out.println(fileStatus.getModificationTime());
    System.out.println(fileStatus.getReplication());
    System.out.println(fileStatus.getBlockSize());
    System.out.println(fileStatus.getPath().getName());
// 获取块信息
    BlockLocation[] blockLocations = fileStatus.getBlockLocations();
    System.out.println(Arrays.toString(blockLocations));
  }
// 3 关闭资源
  fs.close();
}
//  HDFS 文件和文件夹判断
@Test
public void testListStatus() throws IOException, InterruptedException,
    URISyntaxException{
  // 1 获取文件配置信息
  Configuration configuration = new Configuration();
  FileSystem fs = FileSystem.get(new URI("hdfs://hadoop100:8020"),
      configuration, "root");
  // 2 判断是文件还是文件夹
  FileStatus[] listStatus = fs.listStatus(new Path("/"));
  for (FileStatus fileStatus : listStatus) {
    // 如果是文件
    if (fileStatus.isFile()) {
      System.out.println("f:"+fileStatus.getPath().getName());
    }else {
      System.out.println("d:"+fileStatus.getPath().getName());
    }
  }
  // 3 关闭资源
  fs.close();
}

}

