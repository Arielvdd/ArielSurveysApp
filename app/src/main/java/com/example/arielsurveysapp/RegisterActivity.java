package com.example.arielsurveysapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arielsurveysapp.model.User;
import com.example.arielsurveysapp.services.AuthenticationService;
import com.example.arielsurveysapp.services.DatabaseService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFname, etLname, etPhone, etEmail, etPassword;
    private RadioGroup radioGroupGender, rbRoles;
    private RadioButton rbMale, rbFemale, rbRoleTeacher, rbRoleStudent;
    private Button btnRegister;
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    private static final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        /// get the instance of the authentication service
        authenticationService = AuthenticationService.getInstance();
        /// get the instance of the database service
        
        databaseService = DatabaseService.getInstance();





        initViews();

        btnRegister.setOnClickListener(v -> {
            String firstName = etFname.getText().toString();
            String lastName = etLname.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String gender = getSelectedGender();
            String role = getSelectedRole();

            if (isValidInput(firstName, lastName, phone, email, password, gender, role)) {
                /// Register user
                registerUser(email, password, firstName, lastName, phone);

            }
        });
    }

    private void initViews() {
        etFname = findViewById(R.id.etFname);
        etLname = findViewById(R.id.etLname);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        radioGroupGender = findViewById(R.id.radioGroupGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);

        rbRoles = findViewById(R.id.rbRoles);
        rbRoleTeacher = findViewById(R.id.rbRoleTeacher);
        rbRoleStudent = findViewById(R.id.rbRoleStudent);

        btnRegister = findViewById(R.id.btnRegister);
    }

    private String getSelectedGender() {
        int selectedId = radioGroupGender.getCheckedRadioButtonId();
        if (selectedId == rbMale.getId()) {
            return "Male";
        } else if (selectedId == rbFemale.getId()) {
            return "Female";
        }
        return "";
    }

    private String getSelectedRole() {
        int selectedId = rbRoles.getCheckedRadioButtonId();
        if (selectedId == rbRoleTeacher.getId()) {
            return "Teacher";
        } else if (selectedId == rbRoleStudent.getId()) {
            return "Student";
        }
        return "";
    }

    private boolean isValidInput(String firstName, String lastName, String phone, String email, String password, String gender, String role) {
        if (firstName.isEmpty()) {
            showToast("First name is required");
            return false;
        }
        if (lastName.isEmpty()) {
            showToast("Last name is required");
            return false;
        }
        if (phone.isEmpty()) {
            showToast("Phone number is required");
            return false;
        }
        if (!isValidPhoneNumber(phone)) {
            showToast("Please enter a valid phone number");
            return false;
        }
        if (email.isEmpty()) {
            showToast("Email is required");
            return false;
        }
        if (!isValidEmail(email)) {
            showToast("Please enter a valid email address");
            return false;
        }
        if (password.isEmpty()) {
            showToast("Password is required");
            return false;
        }
        if (password.length() < 6) {
            showToast("Password must be at least 6 characters long");
            return false;
        }
        if (gender.isEmpty()) {
            showToast("Please select a gender");
            return false;
        }
        if (role.isEmpty()) {
            showToast("Please select a role");
            return false;
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        return pattern.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.length() == 10 && phone.startsWith("05");
    }

    /// Register the user
    private void registerUser(String email, String password, String fName, String lName, String phone,String gendet, String role,String) {
        Log.d(TAG, "registerUser: Registering user...");

        /// call the sign up method of the authentication service
        authenticationService.signUp(email, password, new AuthenticationService.AuthCallback<String>() {

            @Override
            public void onCompleted(String uid) {
                Log.d(TAG, "onCompleted: User registered successfully");
                /// create a new user object
                User user = new User();
                user.setId(uid);
                user.setEmail(email);
                user.setPassword(password);
                user.setFname(fName);
                user.setLname(lName);
                user.setPhone(phone);

                /// call the createNewUser method of the database service
                databaseService.createNewUser(user, new DatabaseService.DatabaseCallback<Void>() {

                    @Override
                    public void onCompleted(Void object) {
                        Log.d(TAG, "onCompleted: User registered successfully");
                        /// save the user to shared preferences
                     //   SharedPreferencesUtil.saveUser(RegisterActivity.this, user);
                        Log.d(TAG, "onCompleted: Redirecting to MainActivity");
                        /// Redirect to MainActivity and clear back stack to prevent user from going back to register screen
                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        /// clear the back stack (clear history) and start the MainActivity
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to register user", e);
                        /// show error message to user
                        Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
                        /// sign out the user if failed to register
                        /// this is to prevent the user from being logged in again
                        authenticationService.signOut();
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to register user", e);
                /// show error message to user
                Toast.makeText(RegisterActivity.this, "Failed to register user", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
