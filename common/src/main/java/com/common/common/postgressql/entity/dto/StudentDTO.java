package com.common.common.postgressql.entity.dto;


import com.common.common.postgressql.entity.School;
import com.common.common.postgressql.entity.Subject;
import java.io.Serializable;
import java.util.List;

/**
 * @ProjectName: postgres
 * @Package: com.example.postgres.entity.dto
 * @ClassName: StudentDTO
 * @Author: guohailong
 * @Description:
 * @Date: 2021/3/31 20:36
 * @Version: 1.0
 */
public class StudentDTO implements Serializable {

    private Long id;

    private String studentName;

    private School school;

    private List<Subject> subjectList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public List<Subject> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<Subject> subjectList) {
        this.subjectList = subjectList;
    }
}
