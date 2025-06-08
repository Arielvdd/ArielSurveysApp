package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.adapters.SurveyStatsAdapter;
import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.SurveyResult;
import com.example.arielsurveysapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SurveyStatsActivity extends AppCompatActivity {

    DatabaseService databaseService;
    RecyclerView rvSurveysStats;
    String surveyId;
    List<Question> questionsList = new ArrayList<>();
    ArrayList<HashMap<String, Integer>> questionStats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_survey_stats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseService = DatabaseService.getInstance();
        surveyId = getIntent().getStringExtra("surveyId");

        rvSurveysStats = findViewById(R.id.rv_survey_stats);
        rvSurveysStats.setLayoutManager(new LinearLayoutManager(this));

        databaseService.getQuestionsForSurvey(surveyId, new DatabaseService.DatabaseCallback<List<Question>>() {
            @Override
            public void onCompleted(List<Question> questions) {
                questionsList.addAll(questions);
            }

            @Override
            public void onFailed(Exception e) {
            }
        });

        databaseService.getSurveyResults(surveyId, new DatabaseService.DatabaseCallback<SurveyResult>() {
            @Override
            public void onCompleted(SurveyResult surveyResult) {

                if(surveyResult != null) {


                    for (List<String> aggregatedAnswer : surveyResult.getAggregatedAnswers()) {
                        HashMap<String, Integer> arrayCounter = new HashMap<>();
                        for (String answer : aggregatedAnswer) {
                            arrayCounter.put(answer, arrayCounter.getOrDefault(answer, 0) + 1);
                        }
                        questionStats.add(arrayCounter);
                    }
                    ShowQuestions();
                }
            }

            @Override
            public void onFailed(Exception e) {
            }
        });
    }

    private void ShowQuestions() {
        if (!questionsList.isEmpty() && !questionStats.isEmpty()) {
            SurveyStatsAdapter adapter = new SurveyStatsAdapter(questionStats,questionsList);
            rvSurveysStats.setAdapter(adapter);
        }
    }
}
