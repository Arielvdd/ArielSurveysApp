package com.example.arielsurveysapp.adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private final List<Question> questions;
    private final List<String> answers;


    public QuestionAdapter(List<Question> questions) {
        this.questions = questions;
        this.answers = new ArrayList<>(Collections.nCopies(questions.size(), ""));
    }

    public List<String> getUserAnswers() {
        return answers;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questions.get(position);
        holder.tvQuestionText.setText((position + 1) + ". " + question.getQuestionText());

       // holder.rgOptions.setVisibility(View.GONE);
      //  holder.etOpenAnswer.setVisibility(View.GONE);
      //  holder.rgOptions.removeAllViews();

        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            holder.rgOptions.setVisibility(View.VISIBLE);

            for (int i = 0; i < question.getOptions().size(); i++) {
                String option = question.getOptions().get(i);
                RadioButton radioButton = new RadioButton(holder.itemView.getContext());
                radioButton.setText(option);
                radioButton.setId(View.generateViewId());
                holder.rgOptions.addView(radioButton);

             //   if (option.equals(answers.get(position))) {
             //       radioButton.setChecked(true);
             //   }
            }

            holder.rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
                RadioButton selectedButton = group.findViewById(checkedId);
                if (selectedButton != null) {
                    answers.set(position, selectedButton.getText().toString());
                }
            });
        } else {
            holder.etOpenAnswer.setVisibility(View.VISIBLE);
            holder.etOpenAnswer.setText(answers.get(position));
            holder.etOpenAnswer.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    answers.set(position, s.toString());
                }
                @Override public void afterTextChanged(Editable s) {}
            });
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionText;
        RadioGroup rgOptions;
        EditText etOpenAnswer;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            rgOptions = itemView.findViewById(R.id.rgOptions);
            etOpenAnswer = itemView.findViewById(R.id.etOpenAnswer);
        }
    }

}