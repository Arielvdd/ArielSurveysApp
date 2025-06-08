package com.example.arielsurveysapp.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arielsurveysapp.EditSurvey;
import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.services.DatabaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionEditAdapter extends   RecyclerView.Adapter<QuestionEditAdapter.QuestionViewHolder>{

private final List<Question> questions;

private Context context;
private Survey survery;




public QuestionEditAdapter(List<Question> questions,Context context,Survey survery){
        this.questions=questions;

        this.context=context;
        this.survery=survery;
        }


@NonNull
@Override
public QuestionEditAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question_edit,parent,false);
        return new QuestionEditAdapter.QuestionViewHolder(view);
        }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {


        Question question=questions.get(position);
        holder.tvQuestionText.setText((position+1)+". "+question.getQuestionText());

        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            holder.rgOptionsEdit.setVisibility(View.VISIBLE);

            for (int i = 0; i < question.getOptions().size(); i++) {
                String option = question.getOptions().get(i);
                RadioButton radioButton = new RadioButton(holder.itemView.getContext());
                radioButton.setText(option);
                radioButton.setId(View.generateViewId());
                holder.rgOptionsEdit.addView(radioButton);

            }
        }
        holder.btnDeleteQ.setOnClickListener(v-> {
            if ((context) instanceof EditSurvey) {


              questions.remove(position);


                DatabaseService databaseService = DatabaseService.getInstance();
                databaseService.updateSurvey(survery, new DatabaseService.DatabaseCallback<Void>() {
                    @Override
                    public void onCompleted(Void object) {

                            notifyDataSetChanged();

                    }

                    @Override
                    public void onFailed(Exception e) {

                    }
                });
            }
        });
    }


@Override
public int getItemCount() {
    if (questions != null) {
        return questions.size();
    } else return 0;
}

static class QuestionViewHolder extends RecyclerView.ViewHolder {
    TextView tvQuestionText;

    EditText etOpenAnswer;
    RadioGroup rgOptionsEdit;

    Button btnDeleteQ;

    public QuestionViewHolder(@NonNull View itemView) {
        super(itemView);
        tvQuestionText = itemView.findViewById(R.id.tvQuestionTextEdit);
        btnDeleteQ = itemView.findViewById(R.id.btnItemDeleteEdit);
        rgOptionsEdit=itemView.findViewById(R.id.rgOptionsEdit);

    }
}


}