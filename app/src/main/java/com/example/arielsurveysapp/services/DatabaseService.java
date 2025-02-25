package com.example.arielsurveysapp.services;

import android.util.Log;
import androidx.annotation.Nullable;
import com.example.arielsurveysapp.model.*;
import com.google.firebase.database.*;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static final String TAG = "DatabaseService";
    private static DatabaseService instance;
    private final DatabaseReference databaseReference;

    private DatabaseService() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public static DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    public interface DatabaseCallback<T> {
        void onCompleted(T object);
        void onFailed(Exception e);
    }

    private void writeData(@NotNull final String path, @NotNull final Object data, final @Nullable DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback != null) callback.onCompleted(null);
            } else {
                if (callback != null) callback.onFailed(task.getException());
            }
        });
    }

    private DatabaseReference readData(@NotNull final String path) {
        return databaseReference.child(path);
    }

    private <T> void getData(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    private String generateNewId(@NotNull final String path) {
        return databaseReference.child(path).push().getKey();
    }

    // 🔹 Generate IDs
    public String generateSurveyId() {
        return generateNewId("surveys");
    }

    public String generateQuestionId() {
        return generateNewId("questions");
    }

    public String generateAnswerId() {
        return generateNewId("answers");
    }

    public String generateSurveyResultId() {
        return generateNewId("survey_results");
    }

    // 🔹 Save Users (Students, Teachers)
    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("users/" + user.getId(), user, callback);
    }

    public void createNewTeacher(@NotNull final Teacher teacher, @Nullable final DatabaseCallback<Void> callback) {
        writeData("teachers/" + teacher.getId(), teacher, callback);
    }

    public void createNewStudent(@NotNull final Student student, @Nullable final DatabaseCallback<Void> callback) {
        writeData("students/" + student.getId(), student, callback);
    }

    // 🔹 Save Surveys
    public void createNewSurvey(@NotNull final Survey survey, @Nullable final DatabaseCallback<Void> callback) {
        writeData("surveys/" + survey.getId(), survey, callback);
    }

    public void getSurvey(@NotNull final String surveyId, @NotNull final DatabaseCallback<Survey> callback) {
        getData("surveys/" + surveyId, Survey.class, callback);
    }

    public void getAllSurveys(@NotNull final DatabaseCallback<List<Survey>> callback) {
        readData("surveys").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            List<Survey> surveys = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Survey survey = dataSnapshot.getValue(Survey.class);
                surveys.add(survey);
            });
            callback.onCompleted(surveys);
        });
    }

    // 🔹 Save Questions
    public void createNewQuestion(@NotNull final Question question, @Nullable final DatabaseCallback<Void> callback) {
        writeData("questions/" + question.getId(), question, callback);
    }

    public void getAllQuestions(@NotNull final DatabaseCallback<List<Question>> callback) {
        readData("questions").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            List<Question> questions = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Question question = dataSnapshot.getValue(Question.class);
                questions.add(question);
            });
            callback.onCompleted(questions);
        });
    }

    // 🔹 Save Answers
    public void submitAnswer(@NotNull final Answer answer, @Nullable final DatabaseCallback<Void> callback) {
        String answerId = generateAnswerId();
        writeData("survey_answers/" + answerId, answer, callback);
    }

    // 🔹 Save & Update Survey Results
    public void updateSurveyResults(Answer answer, int questionCount) {
        DatabaseReference resultRef = databaseReference.child("survey_results").child(answer.getSurveyId());

        resultRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                SurveyResult surveyResult;
                if (snapshot.exists()) {
                    surveyResult = snapshot.getValue(SurveyResult.class);
                } else {
                    surveyResult = new SurveyResult(answer.getSurveyId(), questionCount);
                }
                surveyResult.setTotalResponses(surveyResult.getTotalResponses() + 1);

                for (int i = 0; i < answer.getAnswers().size(); i++) {
                    surveyResult.getAggregatedAnswers().get(i).add(answer.getAnswers().get(i));
                }

                resultRef.setValue(surveyResult)
                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "Survey result updated successfully!"))
                        .addOnFailureListener(e -> Log.e("Firebase", "Error updating survey result", e));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Error reading survey results", error.toException());
            }
        });
    }

    public void getSurveyResults(String surveyId, @NotNull final DatabaseCallback<SurveyResult> callback) {
        getData("survey_results/" + surveyId, SurveyResult.class, callback);
    }

    // 🔹 Get Users
    public void getUser(@NotNull final String userId, @NotNull final DatabaseCallback<User> callback) {
        getData("users/" + userId, User.class, callback);
    }

    public void getAllUsers(@NotNull final DatabaseCallback<List<User>> callback) {
        readData("users").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            List<User> users = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                User user = dataSnapshot.getValue(User.class);
                users.add(user);
            });
            callback.onCompleted(users);
        });
    }
}
