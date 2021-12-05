package com.common.common.postgressql.entity;

import java.io.Serializable;

/**
 * (Subject)实体类
 *
 * @author makejava
 * @since 2021-03-31 19:38:34
 */
public class Subject implements Serializable {
    private static final long serialVersionUID = 833532794765739844L;

    private Long id;

    private String subjectName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

}
