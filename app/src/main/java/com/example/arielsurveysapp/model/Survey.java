package com.example.arielsurveysapp.model;

import java.util.List;

public class Survey {
    private String id;
    private String category;
    private String title;
    private String targetGrade;  // מספרי כיתות
    private String targetSection; // שכבות
    private String description;
    private List<Question> questions;


    public Survey(String id, String category, String title, String targetGrade, String targetSection, String description, List<Question> questions) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.targetGrade = targetGrade;
        this.targetSection = targetSection;
        this.description = description;
        this.questions = questions;
    }

    public Survey() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(String targetGrade) {
        this.targetGrade = targetGrade;
    }

    public String getTargetSection() {
        return targetSection;
    }

    public void setTargetSection(String targetSection) {
        this.targetSection = targetSection;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", targetGrade='" + targetGrade + '\'' +
                ", targetSection='" + targetSection + '\'' +
                ", description='" + description + '\'' +
                ", questions=" + questions +
                '}';
    }
}
