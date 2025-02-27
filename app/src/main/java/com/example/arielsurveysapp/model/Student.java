package com.example.arielsurveysapp.model;
public class Student extends User {
    private String studentClass; // שכבה
    private String section; // כיתה

    public Student(String id, String fName, String lName, String phone, String email, String password, String gender, String studentClass, String section) {
        super(id, fName, lName, gender, phone, email, password);
        this.studentClass = studentClass;
        this.section = section;
    }

    public Student() {
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentClass='" + studentClass + '\'' +
                ", section='" + section + '\'' +
                '}';
    }


}
