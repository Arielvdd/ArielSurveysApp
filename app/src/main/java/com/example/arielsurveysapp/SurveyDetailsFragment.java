package com.example.arielsurveysapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.arielsurveysapp.model.Survey;

public class SurveyDetailsFragment extends Fragment {

    private EditText etTitle, etCategory, etDescription;
    private Spinner spinnerGrade, spinnerSection;
    private Button btnNext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_survey_details, container, false);

        etTitle = view.findViewById(R.id.etTitle);
        etCategory = view.findViewById(R.id.etCategory);
        spinnerGrade = view.findViewById(R.id.spinnerGrade);
        spinnerSection = view.findViewById(R.id.spinnerSection);
        etDescription = view.findViewById(R.id.etDescription);
        btnNext = view.findViewById(R.id.btnNext);

        btnNext.setOnClickListener(v -> {
            Survey survey = new Survey();
            survey.setTitle(etTitle.getText().toString());
            survey.setCategory(etCategory.getText().toString());
            survey.setTargetGrade(spinnerGrade.getSelectedItem().toString());
            survey.setTargetSection(spinnerSection.getSelectedItem().toString());
            survey.setDescription(etDescription.getText().toString());

            Bundle bundle = new Bundle();
            bundle.putSerializable("survey", survey);

            AddQuestionsFragment fragment = new AddQuestionsFragment();
            fragment.setArguments(bundle);

            ((CreateSurveyActivity) getActivity()).loadFragment(fragment);
        });

        return view;
    }
}
