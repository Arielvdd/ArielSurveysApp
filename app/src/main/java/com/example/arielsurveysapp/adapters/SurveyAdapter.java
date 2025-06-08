package com.example.arielsurveysapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.AdminDashboardActivity;
import com.example.arielsurveysapp.AssignedSurveysActivity;
import com.example.arielsurveysapp.EditSurvey;
import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.ShowSurveyActivity;
import com.example.arielsurveysapp.SurveyStatsActivity;
import com.example.arielsurveysapp.SurveysActivity;
import com.example.arielsurveysapp.model.Survey;
import java.util.List;

public class SurveyAdapter extends RecyclerView.Adapter<SurveyAdapter.SurveyViewHolder> {

    private List<Survey> surveyList;
    private Context context;

    public SurveyAdapter(List<Survey> surveyList, Context context) {
        this.surveyList = surveyList;
        this.context = context;
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
        if((context) instanceof AssignedSurveysActivity)

                holder.tvSurveyStatus.setText("");
        else  holder.tvSurveyStatus.setText(survey.getStatus());

        holder.itemView.setOnClickListener(v -> {
            if((context) instanceof AssignedSurveysActivity) {
                Intent intent = new Intent(context, ShowSurveyActivity.class);
                intent.putExtra("surveyId", survey.getId());
                context.startActivity(intent);
            }

            if((context) instanceof SurveysActivity || (context) instanceof AdminDashboardActivity) {
                if(survey.getStatus().equals("close")) {
                    Intent intent = new Intent(context, SurveyStatsActivity.class);

                    intent.putExtra("surveyId", survey.getId());
                    context.startActivity(intent);
                }
                else {
                    if(survey.getStatus().equals("open")) {
                        Intent intent = new Intent(context, EditSurvey.class);

                        intent.putExtra("surveyId", survey.getId());
                        context.startActivity(intent);
                    }
                }


            }
            if((context) instanceof AdminDashboardActivity) {
                Intent intent = new Intent(context, SurveyStatsActivity.class);
                intent.putExtra("surveyId", survey.getId());
                context.startActivity(intent);
            }



        });


    }

    @Override
    public int getItemCount() {
        return surveyList.size();
    }

    static class SurveyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory,tvSurveyStatus;

        public SurveyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvSurveyTitle);
            tvCategory = itemView.findViewById(R.id.tvSurveyCategory);
            tvSurveyStatus=itemView.findViewById(R.id.tvSurveyStatus);
        }
    }
}
