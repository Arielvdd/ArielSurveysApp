package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.adapters.SurveyAdapter;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.services.DatabaseService;
import java.util.List;

public class SurveysActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSurveys;
    private Button btnCreateSurvey;
    private SurveyAdapter surveyAdapter;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys);

        recyclerViewSurveys = findViewById(R.id.recyclerViewSurveys);
        btnCreateSurvey = findViewById(R.id.btnCreateSurvey);
        databaseService = DatabaseService.getInstance();

        recyclerViewSurveys.setLayoutManager(new LinearLayoutManager(this));

        btnCreateSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SurveysActivity.this, CreateSurveyActivity.class));
            }
        });

        loadSurveys();
    }

    private void loadSurveys() {
        databaseService.getAllSurveys(new DatabaseService.DatabaseCallback<List<Survey>>() {
            @Override
            public void onCompleted(List<Survey> surveys) {
                surveyAdapter = new SurveyAdapter(surveys);
                recyclerViewSurveys.setAdapter(surveyAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
