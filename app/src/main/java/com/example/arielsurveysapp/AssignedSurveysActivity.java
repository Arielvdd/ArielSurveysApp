package com.example.arielsurveysapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.adapters.SurveyAdapter;
import com.example.arielsurveysapp.model.Answer;
import com.example.arielsurveysapp.model.Student;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.services.AuthenticationService;
import com.example.arielsurveysapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class AssignedSurveysActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAssignedSurveys;
    private SurveyAdapter surveyAdapter;
    private DatabaseService databaseService;

    private Student currentStudent = null;
    private AuthenticationService authenticationService;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assigned_surveys);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        authenticationService = AuthenticationService.getInstance();
        uid = authenticationService.getCurrentUserId();
        databaseService = DatabaseService.getInstance();

        // Get the current student data
        databaseService.getStudent(uid, new DatabaseService.DatabaseCallback<Student>() {
            @Override
            public void onCompleted(Student object) {
                currentStudent = object;
                loadAssignedSurveys();
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });

        recyclerViewAssignedSurveys = findViewById(R.id.recyclerViewAssignedSurveys);
        recyclerViewAssignedSurveys.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadAssignedSurveys() {
        databaseService.getAllSurveys(new DatabaseService.DatabaseCallback<List<Survey>>() {
            @Override
            public void onCompleted(List<Survey> allSurveys) {
                String fullClass = currentStudent.getStudentClass() + currentStudent.getSection(); // e.g. "י5"
                String entireClass = currentStudent.getStudentClass() + "all";
                List<Survey> assignedSurveys = new ArrayList<>();
                for (Survey survey : allSurveys) {


                        if (survey.getTargetGrade() != null) {
                            if (survey.getTargetGrade().contains(fullClass) || survey.getTargetGrade().contains(entireClass))
                                assignedSurveys.add(survey);
                        }

                }

                filterUnansweredSurveys(assignedSurveys);
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void filterUnansweredSurveys(List<Survey> assignedSurveys) {
        List<Survey> surveysToShow = new ArrayList<>();
        int totalSurveys = assignedSurveys.size();
        int[] checkedCount = {0};

        for (Survey survey : assignedSurveys) {
            databaseService.getUserAnswer(survey.getId(), uid, new DatabaseService.DatabaseCallback<Answer>() {
                @Override
                public void onCompleted(Answer answer) {
                    if (answer == null) {
                        surveysToShow.add(survey);
                    }

                    checkedCount[0]++;

                    if (checkedCount[0] == totalSurveys) {
                        updateRecyclerView(surveysToShow);
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    e.printStackTrace();
                    checkedCount[0]++;

                    if (checkedCount[0] == totalSurveys) {
                        updateRecyclerView(surveysToShow);
                    }
                }
            });
        }
    }

    private void updateRecyclerView(List<Survey> surveysToShow) {
        surveyAdapter = new SurveyAdapter(surveysToShow, AssignedSurveysActivity.this);
        recyclerViewAssignedSurveys.setAdapter(surveyAdapter);
    }
}
