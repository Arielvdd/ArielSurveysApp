package com.example.arielsurveysapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.services.DatabaseService;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;

public class SurveyStatsAdapter extends RecyclerView.Adapter<SurveyStatsAdapter.QuestionStatsViewHolder> {
    private final List<Question> questions;
    private final List<HashMap<String, Integer>> questionStats;


    public SurveyStatsAdapter(List<HashMap<String, Integer>> questionStats, List<Question> questions) {
        this.questionStats = questionStats;
        this.questions = questions;
    }


    @NonNull
    @Override
    public QuestionStatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey_stat, parent, false);
        return new QuestionStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionStatsViewHolder holder, int position) {
        holder.tvQuestionNumber.setText("Question " + (position + 1) + ": " + questions.get(position).getQuestionText());
        holder.rvAnswers.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvAnswers.setAdapter(new AnswerStatsAdapter(questionStats.get(position)));
    }

    @Override
    public int getItemCount() {
        return questionStats.size();
    }

    static class QuestionStatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber;
        RecyclerView rvAnswers;

        public QuestionStatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tv_item_survey_stat_q);
            rvAnswers = itemView.findViewById(R.id.item_survey_stat_rv);
        }
    }
}
