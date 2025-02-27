package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherDashboardActivity extends AppCompatActivity {

    private Button btnViewUsers, btnViewSurveys, btnCreateSurvey; // Added btnCreateSurvey

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        btnViewUsers = findViewById(R.id.btnViewUsers);
        btnViewSurveys = findViewById(R.id.btnViewSurveys);
        btnCreateSurvey = findViewById(R.id.btnCreateSurvey); // Initialize button

        btnViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboardActivity.this, UsersActivity.class));
            }
        });

        btnViewSurveys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboardActivity.this, SurveysActivity.class));
            }
        });

        btnCreateSurvey.setOnClickListener(new View.OnClickListener() { // Set click listener
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeacherDashboardActivity.this, CreateSurveyActivity.class));
            }
        });
    }
}
