package com.common.common.hbase;

/**
 * java类简单作用描述
 *
 * @ProjectName: common1
 * @Package: com.common.common.hbase
 * @ClassName: Student
 * @Description: java类作用描述
 * @Author: zhangq
 * @CreateDate: 2021-11-22 15:29
 * @UpdateUser: zhangq
 * @UpdateDate: 2021-11-22 15:29
 * @UpdateRemark: The modified content
 * @Version: 1.0 *
 */
public class Student {
  private String id;
  private String name;
  private String age;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAge() {
    return age;
  }

  public void setAge(String age) {
    this.age = age;
  }

  @Override
  public String toString() {
    return "Student{" +
        "id='" + id + '\'' +
        ", name='" + name + '\'' +
        ", age=" + age +
        '}';
  }
}
