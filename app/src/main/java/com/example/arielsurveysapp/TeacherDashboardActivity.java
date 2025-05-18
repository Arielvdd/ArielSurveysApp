package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnViewUsers, btnViewSurveys, btnCreateSurvey, btnPublishSurveys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        btnCreateSurvey = findViewById(R.id.btnCreateSurvey);
        btnViewSurveys = findViewById(R.id.btnViewSurveys);
        btnViewUsers = findViewById(R.id.btnViewUsers);
        btnPublishSurveys = findViewById(R.id.btnPublishSurveys);

        btnCreateSurvey.setOnClickListener(this);
        btnViewSurveys.setOnClickListener(this);
        btnViewUsers.setOnClickListener(this);
        btnPublishSurveys.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        if (v.getId() == R.id.btnCreateSurvey) {
            intent = new Intent(this, CreateSurveyActivity.class);
        } else if (v.getId() == R.id.btnViewSurveys) {
            intent = new Intent(this, SurveysActivity.class);
        } else if (v.getId() == R.id.btnViewUsers) {
            intent = new Intent(this, UsersActivity.class);
        } else if (v.getId() == R.id.btnPublishSurveys) {
            intent = new Intent(this, MainActivity.class);
        }

        if (intent != null) {
            startActivity(intent);
        }
    }
}