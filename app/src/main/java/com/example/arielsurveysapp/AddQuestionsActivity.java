package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddQuestionsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSurveyTitleAddQ, tvSurveyCategoryAddQ;
    private EditText etQuestionTextAddQ, etQuestionOption1AddQ;
    private Button btnCreateNewOption, btnSaveQ, btnAddQ;
    private LinearLayout optionsContainer;
    private ArrayList<EditText> optionEditTexts = new ArrayList<>();

    List<String> options = new ArrayList<>();

    Intent takeit;
    Survey survey;
    DatabaseService databaseService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_questions);
        initViews();
        databaseService=DatabaseService.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        takeit = getIntent();
        survey = (Survey) takeit.getSerializableExtra("survey");
        if (survey != null) {
            tvSurveyTitleAddQ.setText(survey.getTitle());
            tvSurveyCategoryAddQ.setText(survey.getCategory());
        }
    }

    private void initViews() {
        tvSurveyTitleAddQ = findViewById(R.id.tvSurveyTitleAddQ);
        tvSurveyCategoryAddQ = findViewById(R.id.tvSurveyCategoryAddQ);
        etQuestionTextAddQ = findViewById(R.id.etQuestionTextAddQ);
        etQuestionOption1AddQ = findViewById(R.id.etQuestionOption1AddQ);
        btnCreateNewOption = findViewById(R.id.btnCreateNewOption);
        btnSaveQ = findViewById(R.id.btnSaveQ);
        optionsContainer = findViewById(R.id.optionsContainer);
        btnAddQ=findViewById(R.id.btnNewQuestion);
        optionEditTexts.add(etQuestionOption1AddQ);


        btnCreateNewOption.setOnClickListener(this);
        btnSaveQ.setOnClickListener(this);
        btnAddQ.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnCreateNewOption) {
            addNewOptionEditText();
        } else if (v == btnSaveQ) {

            saveQuestionOptions();
            clearInputs();
            Intent go = new Intent(AddQuestionsActivity.this,TeacherDashboardActivity.class);
            startActivity(go);
        }

        if(  v==btnAddQ){

            saveQuestionOptions();
            clearInputs();
            recreate();


            etQuestionOption1AddQ.setText("");

        }
    }

    private void addNewOptionEditText() {
        EditText newOptionEditText = new EditText(this);
        newOptionEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        newOptionEditText.setHint("Enter option " + (optionEditTexts.size() + 1));

        optionsContainer.addView(newOptionEditText);
        optionEditTexts.add(newOptionEditText);

//        String optionText = newOptionEditText.getText().toString().trim();
//      if (!optionText.isEmpty()) {
//
//         options.add(optionText);
//        }


    }

    private void saveQuestionOptions() {
     //   String optionText1 = etQuestionOption1AddQ.getText().toString().trim();
    // options.add(optionText1);

        String questionText = etQuestionTextAddQ.getText().toString().trim();


        if (questionText.isEmpty() ) {
            Toast.makeText(this, "Question and options cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        String questionId = DatabaseService.getInstance().generateQuestionId();

        Question question = new Question(questionId, questionText, new ArrayList<>());
        for (EditText optionEditText : optionEditTexts) {
            String optionText = optionEditText.getText().toString().trim();
            if (!optionText.isEmpty()) {
                question.addOption(optionText);
            }
        }






        survey.addQuestionToSurvey(question);



        databaseService.updateSurvey(survey, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Toast.makeText(AddQuestionsActivity.this, "Question saved successfully!", Toast.LENGTH_SHORT).show();
                 // Finish the activity and return to the previous screen
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(AddQuestionsActivity.this, "Failed to save question: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void clearInputs() {
        etQuestionTextAddQ.setText("");
        options.clear();
        for (EditText editText : optionEditTexts) {
            editText.setText("");
        }
        optionEditTexts.clear();
        optionEditTexts.add(etQuestionOption1AddQ); // Re-add the first default option
    }
}
