package com.example.arielsurveysapp.model;

import java.util.List;

public class Answer {

    protected String surveyId;

    protected String studentId;
    protected List<String> answers;

    public Answer() {
    }

    public Answer(String surveyId, String studentId, List<String> answers) {
        this.surveyId = surveyId;
        this.studentId = studentId;
        this.answers = answers;

    }




    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }


    @Override
    public String toString() {
        return "Answer{" +
                "surveyId='" + surveyId + '\'' +
                ", studentId='" + studentId + '\'' +
                ", answers=" + answers +
                '}';
    }
}
