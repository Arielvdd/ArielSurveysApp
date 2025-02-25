package com.example.arielsurveysapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.adapters.QuestionAdapter;
import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.Survey;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SurveyActivity extends AppCompatActivity {

    private TextView tvSurveyTitle;
    private RecyclerView rvQuestions;
    private Button btnSubmitSurvey;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList;
    private String surveyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        tvSurveyTitle = findViewById(R.id.tvSurveyTitle);
        rvQuestions = findViewById(R.id.rvQuestions);
        btnSubmitSurvey = findViewById(R.id.btnSubmitSurvey);

        rvQuestions.setLayoutManager(new LinearLayoutManager(this));
        questionList = new ArrayList<>();
        questionAdapter = new QuestionAdapter(questionList);
        rvQuestions.setAdapter(questionAdapter);

        surveyId = getIntent().getStringExtra("surveyId");
        loadSurvey(surveyId);

        btnSubmitSurvey.setOnClickListener(view -> submitSurvey());
    }

    private void loadSurvey(String surveyId) {
        DatabaseReference surveyRef = FirebaseDatabase.getInstance().getReference("surveys").child(surveyId);
        surveyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Survey survey = snapshot.getValue(Survey.class);
                if (survey != null) {
                    tvSurveyTitle.setText(survey.getTitle());
                    questionList.clear();
                    questionList.addAll(survey.getQuestions());
                    questionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SurveyActivity.this, "Failed to load survey", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitSurvey() {
        // TODO: Implement answer submission to Firebase
        Toast.makeText(this, "Survey Submitted!", Toast.LENGTH_SHORT).show();
    }
}
