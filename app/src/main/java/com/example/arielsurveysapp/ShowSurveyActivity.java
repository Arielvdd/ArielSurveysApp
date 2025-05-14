package com.example.arielsurveysapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.adapters.QuestionAdapter;
import com.example.arielsurveysapp.model.Answer;
import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.Student;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.model.SurveyResult;
import com.example.arielsurveysapp.services.AuthenticationService;
import com.example.arielsurveysapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;

public class ShowSurveyActivity extends AppCompatActivity {

    private TextView tvSurveyTitle, tvSurveyDescription;
    private RecyclerView recyclerViewQuestions;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questions = new ArrayList<>();

    private String surveyId, uid;
    private Button submitBtn;

    private AuthenticationService authenticationService;
    private DatabaseService databaseService;
    private Survey survey = null;
    Student student=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_survey);

        authenticationService = AuthenticationService.getInstance();
        uid = authenticationService.getCurrentUserId();
        databaseService = DatabaseService.getInstance();


        databaseService.getStudent(uid, new DatabaseService.DatabaseCallback<Student>() {
            @Override
            public void onCompleted(Student object) {
                student=object;

            }

            @Override
            public void onFailed(Exception e) {

            }
        });

        tvSurveyTitle = findViewById(R.id.tvSurveyTitle);
        tvSurveyDescription = findViewById(R.id.tvSurveyDescription);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        submitBtn = findViewById(R.id.btnSubmitAnswers);

        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        surveyId = getIntent().getStringExtra("surveyId");

        loadSurvey(surveyId);

        submitBtn.setOnClickListener(v -> handleSubmit());
    }

    private void loadSurvey(String surveyId) {
        databaseService.getSurvey(surveyId, new DatabaseService.DatabaseCallback<Survey>() {
            @Override
            public void onCompleted(Survey object) {
                survey = object;
                if (survey != null) {
                    if(survey.getTargetGrade().contains(student.getStudentClass())) {
                        tvSurveyTitle.setText(survey.getTitle());
                        tvSurveyDescription.setText(survey.getDescription());
                        questions = (ArrayList<Question>) survey.getQuestions();
                        questionAdapter = new QuestionAdapter(questions);
                        recyclerViewQuestions.setAdapter(questionAdapter);

                    }
                }
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(ShowSurveyActivity.this, "Failed to load survey", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSubmit() {
        List<String> userAnswers = questionAdapter.getUserAnswers();

        if (userAnswers.size() != questions.size()) {
            Toast.makeText(this, "Please answer all questions.", Toast.LENGTH_SHORT).show();
            return;
        }

        Answer answer = new Answer(surveyId, uid, userAnswers);

        databaseService.submitAnswer(answer, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                updateSurveyResult(userAnswers);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(ShowSurveyActivity.this, "Failed to submit answers.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateSurveyResult(List<String> userAnswers) {
        databaseService.getSurveyResults(surveyId, new DatabaseService.DatabaseCallback<SurveyResult>() {
            @Override
            public void onCompleted(SurveyResult result) {
                if (result == null) {
                    result = new SurveyResult(surveyId, questions.size());
                }
                result.addResponse(userAnswers);
                databaseService.updateSurveyResult(result, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {
                        Toast.makeText(ShowSurveyActivity.this, "Answers submitted successfully!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });


            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(ShowSurveyActivity.this, "Failed to update survey results.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
