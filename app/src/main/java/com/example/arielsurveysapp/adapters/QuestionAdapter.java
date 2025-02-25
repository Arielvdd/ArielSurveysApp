package com.example.arielsurveysapp.adapters;

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

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final List<Question> questionList;

    public QuestionAdapter(List<Question> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = questionList.get(position);
        holder.tvQuestionText.setText(question.getQuestionText());

        holder.radioGroup.setVisibility(View.GONE);
        holder.etAnswer.setVisibility(View.GONE);

        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            holder.radioGroup.setVisibility(View.VISIBLE);
            holder.radioGroup.removeAllViews();

            for (String option : question.getOptions()) {
                RadioButton radioButton = new RadioButton(holder.itemView.getContext());
                radioButton.setText(option);
                holder.radioGroup.addView(radioButton);
            }
        } else {
            holder.etAnswer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionText;
        RadioGroup radioGroup;
        EditText etAnswer;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionText = itemView.findViewById(R.id.tvQuestionText);
            radioGroup = itemView.findViewById(R.id.radioGroup);
            etAnswer = itemView.findViewById(R.id.etAnswer);
        }
    }
}
