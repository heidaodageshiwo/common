package com.common.common.flink.stream.key;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

/**
 * 包含成员属性
 * 学生姓名，性别，年龄，以及年级和成绩
 *
 * hl,man,20,4,80,90,100
 *
 * 自定义类注意事项：
 * 1.访问级别：public
 * 2.必须有空构造
 * 3.类中成员属性也必须是public，并且写好getset方法
 * 4.类中属性的类型必须是flink支持的类型
 *
 * 符合上述条件的pojo可以放flink用
 */
public class StudentInfo {
   private String name;
   private String gender;
   private Integer age;
   //将成绩和年级绑定在了一起
   private Tuple2<Integer, Tuple3<Integer,Integer,Integer>> gradeAndScore;

    public StudentInfo() {
    }

    public StudentInfo(String name, String gender, Integer age, Tuple2<Integer, Tuple3<Integer, Integer, Integer>> gradeAndScore) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.gradeAndScore = gradeAndScore;
   }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Tuple2<Integer, Tuple3<Integer, Integer, Integer>> getGradeAndScore() {
        return gradeAndScore;
    }

    public void setGradeAndScore(Tuple2<Integer, Tuple3<Integer, Integer, Integer>> gradeAndScore) {
        this.gradeAndScore = gradeAndScore;
    }

    @Override
    public String toString() {
        return "StudentInfo{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", age=" + age +
                ", gradeAndScore=" + gradeAndScore +
                '}';
    }

}
