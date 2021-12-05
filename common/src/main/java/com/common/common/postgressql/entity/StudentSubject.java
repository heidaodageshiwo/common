package com.common.common.postgressql.entity;

import java.io.Serializable;

/**
 * (StudentSubject)实体类
 *
 * @author makejava
 * @since 2021-03-31 19:38:33
 */
public class StudentSubject implements Serializable {
    private static final long serialVersionUID = 204362454766328190L;

    private Long studentId;

    private Long subjectId;


    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

}
