package com.common.common.postgressql.entity;

import java.io.Serializable;

/**
 * (School)实体类
 *
 * @author makejava
 * @since 2021-03-31 19:38:32
 */
public class School implements Serializable {
    private static final long serialVersionUID = -37158645696826488L;

    private Long id;

    private String schoolName;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

}
