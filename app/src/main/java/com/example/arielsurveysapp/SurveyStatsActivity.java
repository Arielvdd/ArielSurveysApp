package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.arielsurveysapp.model.SurveyResult;
import com.example.arielsurveysapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SurveyStatsActivity extends AppCompatActivity {

    DatabaseService databaseService;

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


        databaseService.getAllSurveyResults(new DatabaseService.DatabaseCallback<List<SurveyResult>>() {
            @Override
            public void onCompleted(List<SurveyResult> surveyResults) {
                Log.d("!!!!!!!!!!!!!!", "surveyResults=" + surveyResults.size());
                // SurveyId -> collection of Array Counter
                HashMap<String, ArrayList<HashMap<String, Integer>>> surveys = new HashMap<>();
                for (SurveyResult surveyResult : surveyResults) {
                    /// question list of Array Counter
                    ArrayList<HashMap<String, Integer>> question = new ArrayList<>();
                    for (List<String> aggregatedAnswer : surveyResult.getAggregatedAnswers()) {
                        // Counter Array
                        HashMap<String, Integer> arrayCounter = new HashMap<>();
                        for (String answer : aggregatedAnswer) {
                            /// increment the amount that users answer this question
                            arrayCounter.put(answer, arrayCounter.getOrDefault(answer, 0) + 1);
                        }
                        question.add(arrayCounter);
                    }
                    surveys.put(surveyResult.getSurveyId(), question);
                }

                showQuestions(surveys);
            }

            @Override
            public void onFailed(Exception e) {

            }
        });
    }

    private void showQuestions(HashMap<String, ArrayList<HashMap<String, Integer>>> surveys) {
        for (Map.Entry<String, ArrayList<HashMap<String, Integer>>> aggregatedAnswer : surveys.entrySet()) {
            String surveyId = aggregatedAnswer.getKey();
            ArrayList<HashMap<String, Integer>> question = aggregatedAnswer.getValue();


            ///
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("surveyId", surveyId);
            intent.putExtra("aggregatedAnswer", question);
        }
    }
}