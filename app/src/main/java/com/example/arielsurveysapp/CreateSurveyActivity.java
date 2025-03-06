package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.services.DatabaseService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateSurveyActivity extends AppCompatActivity {
    private EditText etCategory, etTitle, etDescription, etSelectedClasses;
    private Spinner spinnerClass, spinnerSection;
    private Button btnNext, btnAdd;
    private DatabaseService databaseService;

    private List<String> selectedClassesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_survey);
        databaseService = DatabaseService.getInstance();
        initViews();
    }

    private void initViews() {
        etCategory = findViewById(R.id.etCategory);
        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerSection = findViewById(R.id.spinnerSection);
        etSelectedClasses = findViewById(R.id.etSelectedClasses);
        btnAdd = findViewById(R.id.button);
        btnNext = findViewById(R.id.btnNext);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelectedClass();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSurvey();
            }
        });

        etSelectedClasses.addTextChangedListener(new TextWatcher() {
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

        if (!selectedClassesList.contains(newEntry)) {
            selectedClassesList.add(newEntry);
            etSelectedClasses.setText(String.join(", ", selectedClassesList));
        }
    }

    private void updateSelectedClassesList(String text) {
        selectedClassesList = new ArrayList<>(Arrays.asList(text.split(", ")));
    }

    private void createSurvey() {
        String category = etCategory.getText().toString().trim();
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String classes = etSelectedClasses.getText().toString().trim();

        if (category.isEmpty() || title.isEmpty() || classes.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String surveyId = databaseService.generateSurveyId();
        Survey survey = new Survey(surveyId, category, title, classes, "", description, null);

        databaseService.createNewSurvey(survey, new DatabaseService.DatabaseCallback<Void>() {
            @Override
            public void onCompleted(Void object) {
                Intent intent = new Intent(CreateSurveyActivity.this, AddQuestionsActivity.class);
                intent.putExtra("surveyId", surveyId);
                intent.putExtra("survey", survey);
                startActivity(intent);
            }

            @Override
            public void onFailed(Exception e) {
                Toast.makeText(CreateSurveyActivity.this, "Failed to create survey", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public List<String> getSelectedClassesList() {
        return new ArrayList<>(selectedClassesList);
    }
}
