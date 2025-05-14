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

    // Example: You should pass this from login or shared preferences.
    private Student currentStudent=null;

    private AuthenticationService authenticationService;

    private String uid;

    boolean found = false;


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


        databaseService.getStudent(uid, new DatabaseService.DatabaseCallback<Student>() {
            @Override
            public void onCompleted(Student object) {
                currentStudent=object;

            }

            @Override
            public void onFailed(Exception e) {

            }
        });

        recyclerViewAssignedSurveys = findViewById(R.id.recyclerViewAssignedSurveys);
        recyclerViewAssignedSurveys.setLayoutManager(new LinearLayoutManager(this));
        databaseService = DatabaseService.getInstance();



        loadAssignedSurveys();
    }

    private void loadAssignedSurveys() {
        databaseService.getAllSurveys(new DatabaseService.DatabaseCallback<List<Survey>>() {
            @Override
            public void onCompleted(List<Survey> allSurveys) {
                String fullClass = currentStudent.getStudentClass() + currentStudent.getSection(); // e.g. "י5"

                List<Survey> assignedSurveys = new ArrayList<>();
                for (Survey survey : allSurveys) {
                    if (survey.getTargetGrade() != null && survey.getTargetGrade().contains(fullClass)) {

                       if(!checkAnswerExist(survey)) {
                           assignedSurveys.add(survey);

                           //Toast.makeText(AssignedSurveysActivity.this,found+"",Toast.LENGTH_LONG).show();

                       }
                       // Toast.makeText(AssignedSurveysActivity.this,found+"",Toast.LENGTH_LONG).show();


                    }


                }

                surveyAdapter = new SurveyAdapter(assignedSurveys,AssignedSurveysActivity.this);
                recyclerViewAssignedSurveys.setAdapter(surveyAdapter);
            }

            @Override
            public void onFailed(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public  boolean checkAnswerExist( Survey survey){

        found=false;

        databaseService.getUserAnswer(survey.getId(), uid, new DatabaseService.DatabaseCallback<Answer>() {
            @Override
            public void onCompleted(Answer object) {
                if(object!=null){

                //    object.getSurveyId().equals(survey.getId());
                   found=true;

                    Toast.makeText(AssignedSurveysActivity.this,found+"hghhhhh",Toast.LENGTH_LONG).show();


                }
                else  found=false;




            }

            @Override
            public void onFailed(Exception e) {

                found=false;
            }
        });
        return found;
    }
}
