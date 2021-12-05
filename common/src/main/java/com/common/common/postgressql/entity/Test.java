package com.common.common.postgressql.entity;

import java.io.Serializable;

/**
 * (Test)实体类
 *
 * @author makejava
 * @since 2021-03-31 16:54:52
 */
public class Test implements Serializable {
    private static final long serialVersionUID = 309855904477876468L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 其他
     */
    private Object oter;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Object getOter() {
        return oter;
    }

    public void setOter(Object oter) {
        this.oter = oter;
    }

}
