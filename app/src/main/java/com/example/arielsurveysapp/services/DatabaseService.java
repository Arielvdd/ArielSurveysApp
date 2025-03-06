package com.example.arielsurveysapp.services;

import android.util.Log;
import androidx.annotation.Nullable;
import com.example.arielsurveysapp.model.*;
import com.google.firebase.database.*;
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

    private void writeData(final String path, final Object data, final DatabaseCallback<Void> callback) {
        databaseReference.child(path).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (callback != null) callback.onCompleted(null);
            } else {
                if (callback != null) callback.onFailed(task.getException());
            }
        });
    }

    private DatabaseReference readData(final String path) {
        return databaseReference.child(path);
    }

    private <T> void getData(final String path, final Class<T> clazz, final DatabaseCallback<T> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            T data = task.getResult().getValue(clazz);
            callback.onCompleted(data);
        });
    }

    private String generateNewId(final String path) {
        return databaseReference.child(path).push().getKey();
    }

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

    public void createNewUser(final User user, final DatabaseCallback<Void> callback) {
        writeData("users/" + user.getId(), user, callback);
    }

    public void createNewTeacher(final Teacher teacher, final DatabaseCallback<Void> callback) {
        writeData("teachers/" + teacher.getId(), teacher, callback);
    }

    public void createNewStudent(final Student student, final DatabaseCallback<Void> callback) {
        writeData("students/" + student.getId(), student, callback);
    }

    // 🔹 Fetch all Students
    public void getAllStudents(DatabaseCallback<List<User>> callback) {
        databaseReference.child("students").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<User> students = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User student = data.getValue(User.class);
                    if (student != null) students.add(student);
                }
                callback.onCompleted(students);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onFailed(error.toException());
            }
        });
    }

    // 🔹 Fetch all Teachers
    public void getAllTeachers(DatabaseCallback<List<User>> callback) {
        databaseReference.child("teachers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<User> teachers = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    User teacher = data.getValue(Teacher.class);
                    if (teacher != null) teachers.add(teacher);
                }
                callback.onCompleted(teachers);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                callback.onFailed(error.toException());
            }
        });
    }

    // 🔹 Fetch all Users (Students + Teachers)
    public void getAllUsers(DatabaseCallback<List<User>> callback) {
        List<User> allUsers = new ArrayList<>();

        getAllStudents(new DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> students) {
                allUsers.addAll(students);

                getAllTeachers(new DatabaseCallback<List<User>>() {
                    @Override
                    public void onCompleted(List<User> teachers) {
                        allUsers.addAll(teachers);
                        callback.onCompleted(allUsers);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        callback.onFailed(e);
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                callback.onFailed(e);
            }
        });
    }

    public void createNewSurvey(final Survey survey, final DatabaseCallback<Void> callback) {
        writeData("surveys/" + survey.getId(), survey, callback);
    }

    public void updateSurvey(final Survey survey, final DatabaseCallback<Void> callback) {
        writeData("surveys/" + survey.getId(), survey, callback);
    }



    public void getSurvey(final String surveyId, final DatabaseCallback<Survey> callback) {
        getData("surveys/" + surveyId, Survey.class, callback);
    }

    public void getAllSurveys(final DatabaseCallback<List<Survey>> callback) {
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

    public void submitAnswer(final Answer answer, final DatabaseCallback<Void> callback) {
        String answerId = generateAnswerId();
        writeData("survey_answers/" + answerId, answer, callback);
    }

    public void getSurveyResults(String surveyId, final DatabaseCallback<SurveyResult> callback) {
        getData("survey_results/" + surveyId, SurveyResult.class, callback);
    }
}
