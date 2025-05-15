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

import com.example.arielsurveysapp.model.SurveyResult;
import com.example.arielsurveysapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyStatsActivity extends AppCompatActivity {

    DatabaseService databaseService;
    RecyclerView rvSurveysStats;

    String surveyId;

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


        databaseService.getSurveyResults(surveyId, new DatabaseService.DatabaseCallback<SurveyResult>() {
            @Override
            public void onCompleted(SurveyResult surveyResult) {
                /// question list of Array Counter
                ArrayList<HashMap<String, Integer>> sr = new ArrayList<>();
                for (List<String> aggregatedAnswer : surveyResult.getAggregatedAnswers()) {
                    // Counter Array
                    HashMap<String, Integer> arrayCounter = new HashMap<>();
                    for (String answer : aggregatedAnswer) {
                        /// increment the amount that users answer this question
                        arrayCounter.put(answer, arrayCounter.getOrDefault(answer, 0) + 1);
                    }
                    sr.add(arrayCounter);
                }
                showQuestions(sr);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void showQuestions(ArrayList<HashMap<String, Integer>> question) {

    }
}