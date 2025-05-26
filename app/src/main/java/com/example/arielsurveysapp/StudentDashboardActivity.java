package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StudentDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView titleTextView;
    private Button mySurveysBtn, myDetailsBtn, logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        titleTextView = findViewById(R.id.title);
        mySurveysBtn = findViewById(R.id.mySurveysBtn);
        myDetailsBtn = findViewById(R.id.myDetailsBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        myDetailsBtn.setOnClickListener(this);


        mySurveysBtn.setOnClickListener(this);


        logoutBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==myDetailsBtn){
            Intent go = new Intent(StudentDashboardActivity.this, UserDetails.class);
            startActivity(go);



        }
        if(v==mySurveysBtn){
            Intent go = new Intent(StudentDashboardActivity.this,AssignedSurveysActivity.class);
            startActivity(go);

        }

        if(v==logoutBtn){



        }


    }
}
