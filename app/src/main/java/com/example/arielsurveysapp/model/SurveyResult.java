package com.example.arielsurveysapp.model;

import java.util.ArrayList;
import java.util.List;

public class SurveyResult {
    private String surveyId;
    private int totalResponses;
    private List<List<String>> aggregatedAnswers;

    public SurveyResult() {
    }

    public SurveyResult(String surveyId, int questionCount) {
        this.surveyId = surveyId;
        this.totalResponses = 0;
        this.aggregatedAnswers = new ArrayList<>();

        for (int i = 0; i < questionCount; i++) {
            aggregatedAnswers.add(new ArrayList<>());
        }
    }
    public void addResponse(List<String> answers) {
        totalResponses++;

        for (int i = 0; i < answers.size(); i++) {
            aggregatedAnswers.get(i).add(answers.get(i)); // Store answer in corresponding list
        }
    }

    public String getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(String surveyId) {
        this.surveyId = surveyId;
    }

    public int getTotalResponses() {
        return totalResponses;
    }

    public void setTotalResponses(int totalResponses) {
        this.totalResponses = totalResponses;
    }

    public List<List<String>> getAggregatedAnswers() {
        return aggregatedAnswers;
    }

    public void setAggregatedAnswers(List<List<String>> aggregatedAnswers) {
        this.aggregatedAnswers = aggregatedAnswers;
    }

    @Override
    public String toString() {
        return "SurveyResult{" +
                "surveyId='" + surveyId + '\'' +
                ", totalResponses=" + totalResponses +
                ", aggregatedAnswers=" + aggregatedAnswers +
                '}';
    }
}
