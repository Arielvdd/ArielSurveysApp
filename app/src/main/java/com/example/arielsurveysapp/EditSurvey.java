package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.adapters.QuestionAdapter;
import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditSurvey extends AppCompatActivity {

    Intent takeit;
    DatabaseService databaseService;
    Survey survey = null;
    String surveyId;
    private RecyclerView recyclerViewQuestions;
    private QuestionAdapter questionAdapter;
    private ArrayList<Question> questions = new ArrayList<>();




    private EditText etTitleEdit, etCategoryEdit, etDescriptionEdit, etSelectedClassesEdit;
    private Button btnAdd, btnNext;
    private Spinner spinnerSection,spinnerClass;
    Switch publishSwitchEdit;
    private List<String> selectedClassesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_survey);


        databaseService = DatabaseService.getInstance();
        initViews();

        takeit = getIntent();
        surveyId = takeit.getStringExtra("surveyId");

        if (surveyId != null) {
            loadSurvey(surveyId);
        }




        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelectedClass();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSurvey();
            }
        });

        etSelectedClassesEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateSelectedClassesList(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void addSelectedClass() {
        String selectedClass = spinnerClass.getSelectedItem().toString();
        String selectedSection = spinnerSection.getSelectedItem().toString();
        String newEntry = selectedClass + selectedSection;

        if (!getSelectedClassesList().contains(newEntry)) {
            selectedClassesList.add(newEntry);
            etSelectedClassesEdit.setText(String.join(", ", selectedClassesList));
        }
    }

    private void updateSelectedClassesList(String text) {
        selectedClassesList = new ArrayList<>(Arrays.asList(text.split(", ")));
    }

    private void updateSurvey() {
        String category = etCategoryEdit.getText().toString().trim();
        String title = etTitleEdit.getText().toString().trim();
        String description = etDescriptionEdit.getText().toString().trim();
        String classes = etSelectedClassesEdit.getText().toString().trim();

        if (category.isEmpty() || title.isEmpty() || classes.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        survey.setCategory(category);
        survey.setDescription(description);
        survey.setTargetGrade(classes);
        survey.setTitle(title);
        if(publishSwitchEdit.isChecked())
            survey.setStatus("close");
        else
            survey.setStatus("open");


        databaseService.updateSurvey(survey, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Intent intent = new Intent(EditSurvey.this, AddQuestionsActivity.class);
                intent.putExtra("surveyId", surveyId);
                intent.putExtra("survey", survey);
                startActivity(intent);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(EditSurvey.this, "Failed to create survey", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<String> getSelectedClassesList() {
        return new ArrayList<>(selectedClassesList);
    }



    private void initViews() {

        etTitleEdit = findViewById(R.id.etTitleEdit);
        etCategoryEdit = findViewById(R.id.etCategoryEdit);
        etDescriptionEdit = findViewById(R.id.etDescriptionEdit);
        etSelectedClassesEdit = findViewById(R.id.etSelectedClassesEdit);
        spinnerClass = findViewById(R.id.spinnerClassEdit);
        spinnerSection = findViewById(R.id.spinnerSectionEdit);
        btnAdd = findViewById(R.id.btnAddEdit);
        btnNext = findViewById(R.id.btnNextEdit);
        publishSwitchEdit = findViewById(R.id.switchPublishEdit);
        recyclerViewQuestions=findViewById(R.id.rcQuestionsEdit);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadSurvey(String surveyId) {
        databaseService.getSurvey(surveyId, new DatabaseService.DatabaseCallback<Survey>() {
            @Override
            public void onCompleted(Survey object) {
                survey = object;
                if (survey != null) {
                    etTitleEdit.setText(survey.getTitle());
                    etCategoryEdit.setText(survey.getCategory());
                    etDescriptionEdit.setText(survey.getDescription());
                    etSelectedClassesEdit.setText(survey.getTargetGrade());
                    publishSwitchEdit.setChecked(true);
                    questions = (ArrayList<Question>) survey.getQuestions();
                    questionAdapter = new QuestionAdapter(questions,EditSurvey.this,survey);
                    recyclerViewQuestions.setAdapter(questionAdapter);



                }
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(EditSurvey.this, "Failed to load survey", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
