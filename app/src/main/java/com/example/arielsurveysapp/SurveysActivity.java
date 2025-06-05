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

import java.util.ArrayList;
import java.util.List;

public class SurveysActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSurveys;
    private Button btnCreateSurvey;
    private SurveyAdapter surveyAdapter;
    private DatabaseService databaseService;

    List<Survey>  surveysList=new ArrayList<>();

    Intent takeit;
    String status="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surveys);

        recyclerViewSurveys = findViewById(R.id.recyclerViewSurveys);
        btnCreateSurvey = findViewById(R.id.btnCreateSurvey);
        databaseService = DatabaseService.getInstance();

        recyclerViewSurveys.setLayoutManager(new LinearLayoutManager(this));


        surveyAdapter = new SurveyAdapter(surveysList,this);
        recyclerViewSurveys.setAdapter(surveyAdapter);


        takeit=getIntent();
        status=takeit.getStringExtra("status");
        btnCreateSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SurveysActivity.this, CreateSurveyActivity.class));
            }
        });

        if(status!=null){

            loadSurveys(status);

        }
      else {
            loadAllSurveys();
        }
    }

    private void loadAllSurveys() {

        databaseService.getAllSurveysForAdmin(new DatabaseService.DatabaseCallback<List<Survey>>() {
            @Override
            public void onCompleted(List<Survey> surveys) {
                surveysList.clear();
                surveysList.addAll(surveys);
                surveyAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void loadSurveys(String status) {

        databaseService.getSurveysByStatus(status, new DatabaseService.DatabaseCallback<List<Survey>>() {
            @Override
            public void onCompleted(List<Survey> surveys) {
                surveysList.clear();
                surveysList.addAll(surveys);
                surveyAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }
}
