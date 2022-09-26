package com.common.common.ES8.Builder;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.ES8.Builder
 * @ClassName: BuilderTest
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-26  08:32
 * @UpdateDate: 2022-09-26  08:32
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class BuilderTest {
    //假设这里有比较多的属性

    private String name;
    private Integer age;
    private Integer sex;
    private String address;

    //私有全参构造
    private BuilderTest(String name, Integer age, Integer sex, String address) {
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.address = address;
    }
    //只提供get
    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public Integer getSex() {
        return sex;
    }

    public String getAddress() {
        return address;
    }



    /*提供创建Builder的静态方法*/
    public static BuilderTest.Builder builder(){
        return new BuilderTest.Builder();
    }

    //通过一个内部的Builder来完成对象的构建过程
    public static class Builder{
        private String name;
        private Integer age;
        private Integer sex;
        private String address;

        //只提供set并返回Builder,可链式编程
        public BuilderTest.Builder name(String name) {
            this.name = name;
            return this;
        }

        public BuilderTest.Builder age(Integer age) {
            this.age = age;
            return this;
        }

        public BuilderTest.Builder sex(Integer sex) {
            this.sex = sex;
            return this;
        }

        public BuilderTest.Builder address(String address) {
            this.address = address;
            return this;
        }

        //构建对象
        public BuilderTest build(){
            return new BuilderTest(name,age,sex,address);
        }
    }
}