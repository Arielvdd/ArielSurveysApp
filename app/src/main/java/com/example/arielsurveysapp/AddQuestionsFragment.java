package com.example.arielsurveysapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.Survey;
import java.util.ArrayList;
import java.util.List;

public class AddQuestionsFragment extends Fragment {

    private Survey survey;
    private EditText etQuestion;
    private Button btnAdd, btnFinish;
    private ListView lvQuestions;
    private ArrayAdapter<String> adapter;
    private List<String> questionList;

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_questions, container, false);

        etQuestion = view.findViewById(R.id.etQuestion);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnFinish = view.findViewById(R.id.btnFinish);
        lvQuestions = view.findViewById(R.id.lvQuestions);

        questionList = new ArrayList<>();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, questionList);
        lvQuestions.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> {
            String questionText = etQuestion.getText().toString();
            if (!questionText.isEmpty()) {
                questionList.add(questionText);
                adapter.notifyDataSetChanged();

                Question question = new Question();
                question.setQuestionText(questionText);
                survey.getQuestions().add(question);
            }
        });

        btnFinish.setOnClickListener(v -> {
            // Save to Firebase
        });

        return view;
    }
}
