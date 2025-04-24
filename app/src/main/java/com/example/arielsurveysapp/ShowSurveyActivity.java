package com.example.arielsurveysapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.adapters.QuestionAdapter;
import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.Survey;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShowSurveyActivity extends AppCompatActivity {

    private TextView tvSurveyTitle, tvSurveyDescription;
    private RecyclerView recyclerViewQuestions;
    private QuestionAdapter questionAdapter;

    private String surveyId;
    private DatabaseReference surveysRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_survey);

        tvSurveyTitle = findViewById(R.id.tvSurveyTitle);
        tvSurveyDescription = findViewById(R.id.tvSurveyDescription);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));

        surveyId = getIntent().getStringExtra("surveyId");
        surveysRef = FirebaseDatabase.getInstance().getReference("surveys");

        loadSurvey();
    }

    private void loadSurvey() {
        surveysRef.child(surveyId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Survey survey = snapshot.getValue(Survey.class);
                if (survey != null) {
                    tvSurveyTitle.setText(survey.getTitle());
                    tvSurveyDescription.setText(survey.getDescription());
                    questionAdapter = new QuestionAdapter(survey.getQuestions());
                    recyclerViewQuestions.setAdapter(questionAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
