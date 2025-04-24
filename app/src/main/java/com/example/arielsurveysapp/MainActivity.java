package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnTeachersRegister = findViewById(R.id.btnTeachersRegister);

        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.btnLogin) {
                    Intent intent = new Intent(MainActivity.this, ShowSurveyActivity.class);
                    intent.putExtra("surveyId", "-OObEDvjKTwT-Bm8EcbI");
                    startActivity(intent);
                    //Intent intent = new Intent(MainActivity.this, TeacherDashboardActivity.class);
                    //startActivity(intent);
                } else if (v.getId() == R.id.btnRegister) {
                    Intent intent = new Intent(MainActivity.this, RegisterStudentActivity.class);
                    startActivity(intent);
                } else if (v.getId() == R.id.btnTeachersRegister) {
                    Intent intent = new Intent(MainActivity.this, RegisterTeacherActivity.class);
                    startActivity(intent);
                }
                else if (v.getId() == R.id.btnCreateSurvey) {
                    Intent intent = new Intent(MainActivity.this, CreateSurveyActivity.class);
                    startActivity(intent);
                }
            }
        };

        btnLogin.setOnClickListener(buttonClickListener);
        btnRegister.setOnClickListener(buttonClickListener);
        btnTeachersRegister.setOnClickListener(buttonClickListener);
    }
}
