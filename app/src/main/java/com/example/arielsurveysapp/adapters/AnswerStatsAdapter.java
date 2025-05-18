package com.example.arielsurveysapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnswerStatsAdapter extends RecyclerView.Adapter<AnswerStatsAdapter.AnswerViewHolder> {

    private final List<Map.Entry<String, Integer>> entries;

    public AnswerStatsAdapter(HashMap<String, Integer> answersCount) {
        this.entries = new ArrayList<>(answersCount.entrySet());
    }

    @NonNull
    @Override
    public AnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_survey_stat, parent, false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerViewHolder holder, int position) {
        Map.Entry<String, Integer> entry = entries.get(position);
        holder.tvAnswer.setText(entry.getKey());
        holder.tvCount.setText(String.valueOf(entry.getValue()));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class AnswerViewHolder extends RecyclerView.ViewHolder {
        TextView tvAnswer;
        TextView tvCount;

        public AnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAnswer = itemView.findViewById(R.id.tv_item_item_survey_stat_text);
            tvCount = itemView.findViewById(R.id.tv_item_item_survey_stat_amount);
        }
    }
}
