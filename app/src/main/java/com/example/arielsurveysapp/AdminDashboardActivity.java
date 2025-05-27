package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arielsurveysapp.model.User;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.model.Teacher;
import com.example.arielsurveysapp.services.DatabaseService;
import com.example.arielsurveysapp.adapters.UserAdapter;
import com.example.arielsurveysapp.adapters.SurveyAdapter;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button btnManageUsers;
    private Button btnManageSurveys;
    private Button btnCreateSurvey;
    private Button btnLogout;
    private DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        databaseService = DatabaseService.getInstance();
        initViews();
    }

    private void initViews() {
        btnManageUsers = findViewById(R.id.btnManageUsers);
        btnManageSurveys = findViewById(R.id.btnManageSurveys);
        btnCreateSurvey = findViewById(R.id.btnCreateSurvey);
        btnLogout = findViewById(R.id.btnLogout);

        btnManageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUsersList();
            }
        });

        btnManageSurveys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSurveysList();
            }
        });

        btnCreateSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminDashboardActivity.this, CreateSurveyActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply();
                Intent intent = new Intent(AdminDashboardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showUsersList() {
        databaseService.getAllUsers(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
                builder.setTitle("Users List");

                RecyclerView recyclerView = new RecyclerView(AdminDashboardActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminDashboardActivity.this));
                UserAdapter adapter = new UserAdapter(users);
                recyclerView.setAdapter(adapter);

                builder.setView(recyclerView);
                builder.setNegativeButton("Close", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showSurveysList() {
        databaseService.getAllSurveysForAdmin(new DatabaseService.DatabaseCallback<List<Survey>>() {
            @Override
            public void onCompleted(List<Survey> surveys) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
                builder.setTitle("Surveys List");

                RecyclerView recyclerView = new RecyclerView(AdminDashboardActivity.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(AdminDashboardActivity.this));
                SurveyAdapter adapter = new SurveyAdapter(surveys, AdminDashboardActivity.this);
                recyclerView.setAdapter(adapter);

                builder.setView(recyclerView);
                builder.setNegativeButton("Close", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AdminDashboardActivity.this, "Failed to load surveys", Toast.LENGTH_SHORT).show();
            }
        });
    }
}