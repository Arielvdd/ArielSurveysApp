package com.example.arielsurveysapp.model;

public class Answer {
    private String answerId;
    private String questionId;
    private String studentId;
    private String answerText;

    public Answer(String answerId, String questionId, String studentId, String answerText) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.studentId = studentId;
        this.answerText = answerText;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
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

