package com.example.arielsurveysapp.model;

import androidx.annotation.NonNull;

public class Teacher extends User {
    private String subject; // תחום-לימוד

    public Teacher() {
    }

    public Teacher(String id, String fName, String lName, String phone, String email, String password, String gender, String subject) {
        super(id, fName, lName, gender, phone, email, password);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


    @NonNull
    @Override
    public String toString() {
        return "Teacher{" +
                "subject='" + subject + '\'' +
                '}';
    }
}
