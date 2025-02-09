package com.example.arielsurveysapp.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.arielsurveysapp.model.Question;
import com.example.arielsurveysapp.model.Survey;
import com.example.arielsurveysapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                if (callback != null) callback.onCompleted(task.getResult());
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

    public void createNewUser(@NotNull final User user, @Nullable final DatabaseCallback<Void> callback) {
        writeData("users/" + user.getEmail(), user, callback);
    }

    public void createNewSurvey(@NotNull final Survey survey, @Nullable final DatabaseCallback<Void> callback) {
        writeData("surveys/" + survey.getTitle(), survey, callback);
    }

    public void createNewQuestion(@NotNull final Question question, @Nullable final DatabaseCallback<Void> callback) {
        writeData("questions/" + question.getQuestionText(), question, callback);
    }

    public void getUser(@NotNull final String email, @NotNull final DatabaseCallback<User> callback) {
        getData("users/" + email, User.class, callback);
    }

    public void getSurvey(@NotNull final String title, @NotNull final DatabaseCallback<Survey> callback) {
        getData("surveys/" + title, Survey.class, callback);
    }

    public void getUsers(@NotNull final DatabaseCallback<List<User>> callback) {
        readData("users").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
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

    public void getSurveys(@NotNull final DatabaseCallback<List<Survey>> callback) {
        readData("surveys").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
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

    public void getQuestions(@NotNull final DatabaseCallback<List<Question>> callback) {
        readData("questions").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
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

    public void assignSurveyToUser(@NotNull final String userEmail, @NotNull final String surveyTitle, @Nullable final DatabaseCallback<Void> callback) {
        readData("users/" + userEmail).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user = task.getResult().getValue(User.class);
                if (user != null && !user.getAssignedSurveys().contains(surveyTitle)) {
                    user.getAssignedSurveys().add(surveyTitle);
                    writeData("users/" + userEmail, user, callback);
                }
            } else {
                if (callback != null) callback.onFailed(task.getException());
            }
        });
    }
}
