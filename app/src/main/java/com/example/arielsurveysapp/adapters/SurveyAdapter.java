package com.example.arielsurveysapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.model.Survey;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder> {

    private final List<Survey> surveyList;

    public SurveyAdapter(List<Survey> surveyList) {
        this.surveyList = surveyList;
    }

    @NonNull
    @Override
    public SurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_survey, parent, false);
        return new SurveyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SurveyViewHolder holder, int position) {
        Survey survey = surveyList.get(position);
        holder.tvTitle.setText(survey.getTitle());
        holder.tvCategory.setText(survey.getCategory());
    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    static class SurveyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory;

        public SurveyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSurveyTitle);
            tvCategory = itemView.findViewById(R.id.tvSurveyCategory);
        }
    }
}
