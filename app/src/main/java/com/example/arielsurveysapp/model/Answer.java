package com.example.arielsurveysapp.model;

public class Answer {

    private String surveyId;

    private String questionId;


    private String studentId;
    private String answerText;

    public Answer( String questionId, String studentId, String answerText) {

        this.questionId = questionId;
        this.studentId = studentId;
        this.answerText = answerText;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}

