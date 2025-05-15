package com.example.arielsurveysapp.services;

import android.util.Log;
import androidx.annotation.Nullable;
import com.example.arielsurveysapp.model.*;
import com.google.firebase.database.*;
import com.google.firebase.database.annotations.NotNull;

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

    private <T> void getDataList(@NotNull final String path, @NotNull final Class<T> clazz, @NotNull final DatabaseCallback<List<T>> callback) {
        readData(path).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e(TAG, "Error getting data", task.getException());
                callback.onFailed(task.getException());
                return;
            }
            List<T> tList = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                T t = dataSnapshot.getValue(clazz);
                tList.add(t);
            });

            callback.onCompleted(tList);
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
                if(survey.getStatus().equals("close")) {
                    surveys.add(survey);
                }
            });
            callback.onCompleted(surveys);
        });
    }


    public void getAllSurveysForAdmin(final DatabaseCallback<List<Survey>> callback) {
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

        writeData("survey_answers/" + answer.getSurveyId()+"/"+ answer.getStudentId()+"/", answer, callback);
        writeData("user_SurveyAnswers/" + answer.getStudentId()+"/"+ answer.getSurveyId()+"/", answer, callback);
    }




    public void getUserAnswer(final String surveyId, final String studendId, final DatabaseCallback<Answer> callback) {
        getData("user_SurveyAnswers/" + studendId+"/"+ surveyId,Answer.class, callback);
    }

    public void getUserAnswers(final String uid,   final DatabaseCallback<List<Answer>> callback) {
        readData("user_SurveyAnswers/" + uid).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                callback.onFailed(task.getException());
                return;
            }
            List<Answer> answers = new ArrayList<>();
            task.getResult().getChildren().forEach(dataSnapshot -> {
                Answer answer = dataSnapshot.getValue(Answer.class);
                answers.add(answer);
            });
            callback.onCompleted(answers);
        });
    }



    public void getSurveyResults(String surveyId, final DatabaseCallback<SurveyResult> callback) {
        getData("survey_results/" + surveyId, SurveyResult.class, callback);
    }
    public void updateSurveyResult(SurveyResult result,final DatabaseCallback<Void> callback) {
        writeData("survey_results/" + result.getSurveyId(), result, callback);
    }

    public void getAllSurveyResults(final DatabaseCallback<List<SurveyResult>> callback) {
        getDataList("survey_results", SurveyResult.class, callback);
    }



    /// get a teacher from the database
    /// @param teacherId the id of the teacher to get
    /// @param callback the callback to call when the operation is completed
    ///               the callback will receive the teacher object
    ///              if the operation fails, the callback will receive an exception
    /// @return void
    /// @see DatabaseCallback
    /// @see Teacher
    public void getTeacher(@NotNull final String teacherId, @NotNull final DatabaseCallback<Teacher> callback) {
        getData("teachers/" + teacherId, Teacher.class, callback);
    }

    public void getStudent(@NotNull final String studentId, @NotNull final DatabaseCallback<Student> callback) {
        getData("students/" + studentId, Student.class, callback);
    }




}
