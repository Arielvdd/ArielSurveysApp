package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arielsurveysapp.model.Student;
import com.example.arielsurveysapp.model.Teacher;
import com.example.arielsurveysapp.model.User;
import com.example.arielsurveysapp.services.AuthenticationService;
import com.example.arielsurveysapp.services.DatabaseService;
import com.example.arielsurveysapp.utils.SharedPreferencesUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLog;
    FirebaseAuth mAuth;
    User user=null;






    private static final String TAG = "loginToFireBase";

    public static Student student=null;

    public static Boolean isAdmin=false;
    public  static  Teacher teacher=null;
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /// get the instance of the authentication service
        authenticationService = AuthenticationService.getInstance();
        databaseService=DatabaseService.getInstance();



        etEmail = findViewById(R.id.etEmailLogin);
        etPassword = findViewById(R.id.etPasswordLogin);
        btnLog = findViewById(R.id.btnLogin);


        user= SharedPreferencesUtil.getUser(LoginActivity.this);
        if(user!=null){

            etEmail.setText(user.getEmail());
            etPassword.setText(user.getPassword());
        }

        btnLog.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }
        authenticationService.signIn(email, password, new AuthenticationService.AuthCallback<String>() {
            /// Callback method called when the operation is completed
            ///
            /// @param uid the user ID of the user that is logged in
            @Override
            public void onCompleted(String uid) {
                Log.d(TAG, "onCompleted: User logged in successfully");
                /// get the user data from the database



                            databaseService.getTeacher(uid, new DatabaseService.DatabaseCallback<Teacher>() {
                                @Override
                                public void onCompleted(Teacher obj) {
                                    teacher = obj;

                                    Log.d(TAG, "onCompleted: User data retrieved successfully");
                                    /// save the user data to shared preferences

                                    /// Redirect to main activity and clear back stack to prevent user from going back to login screen
                                    Intent mainIntent = new Intent(LoginActivity.this, TeacherDashboardActivity.class);
                                    /// Clear the back stack (clear history) and start the MainActivity
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                    if (teacher != null) {
                                        SharedPreferencesUtil.saveUser(LoginActivity.this, teacher);
                                        startActivity(mainIntent);

                                    }
                                    else {

                                        databaseService.getStudent(uid, new DatabaseService.DatabaseCallback<Student>() {


                                            @Override
                                            public void onCompleted(Student object) {

                                                student = object;

                                                Log.d(TAG, "onCompleted: User data retrieved successfully");
                                                /// save the user data to shared preferences
                                                SharedPreferencesUtil.saveUser(LoginActivity.this, student);
                                                /// Redirect to main activity and clear back stack to prevent user from going back to login screen
                                                Intent mainIntent = new Intent(LoginActivity.this, StudentDashboardActivity.class);
                                                /// Clear the back stack (clear history) and start the MainActivity
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                            }



                                            @Override
                                            public void onFailed(Exception e) {
                                                Log.e(TAG, "onFailed: Failed to retrieve user data", e);
                                                //getUser

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onFailed(Exception e) {

                                }
                    });
                }

            @Override
            public void onFailed(Exception e) {

                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
            }

            });
    }
}
