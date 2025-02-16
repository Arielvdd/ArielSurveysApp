package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arielsurveysapp.model.Student;
import com.example.arielsurveysapp.model.User;
import com.example.arielsurveysapp.services.AuthenticationService;
import com.example.arielsurveysapp.services.DatabaseService;
import com.example.arielsurveysapp.utils.SharedPreferencesUtil;

public class RegisterStudentActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etFname, etLname, etPhone, etEmail, etPassword;
    private RadioGroup radioGroupGender;
    private RadioButton rbMale, rbFemale;
    private Button btnRegister;
    private AuthenticationService authenticationService;
    private DatabaseService databaseService;

    private static final String TAG = "RegisterStudentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student);

        authenticationService = AuthenticationService.getInstance();
        databaseService = DatabaseService.getInstance();

        initViews();


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

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
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

    private boolean isValidInput(String firstName, String lastName, String phone, String email, String password, String gender) {
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

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(RegisterStudentActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.length() == 10 && phone.startsWith("05");
    }


    @Override
    public void onClick(View v) {


            String firstName = etFname.getText().toString();
            String lastName = etLname.getText().toString();
            String phone = etPhone.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String gender = getSelectedGender();

            if (isValidInput(firstName, lastName, phone, email, password, gender)) {
                registerUser(firstName, lastName, phone, email, password, gender);
            }


    }


    private void registerUser(String firstName, String lastName, String phone, String email, String password, String gender) {
        Log.d(TAG, "registerUser: Registering user...");

        authenticationService.signUp(email, password, new AuthenticationService.AuthCallback<String>() {

            @Override
            public void onCompleted(String uid) {
                Log.d(TAG, "onCompleted: User registered successfully");



                    Student student = new Student(uid, firstName, lastName,phone, email, password,gender,"7777","dguhs");

                databaseService.createNewStudent(student, new DatabaseService.DatabaseCallback<Void>() {

                    @Override
                    public void onCompleted(Void object) {
                        Log.d(TAG, "onCompleted: User saved to database");
                        SharedPreferencesUtil.saveUser(RegisterStudentActivity.this,student);

                        // Redirect to MainActivity after successful registration
                        Intent mainIntent = new Intent(RegisterStudentActivity.this, MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(mainIntent);
                    }

                    @Override
                    public void onFailed(Exception e) {
                        Log.e(TAG, "onFailed: Failed to save user to database", e);
                        showToast("Failed to register user. Please try again.");
                    }
                });
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "onFailed: Failed to register user", e);
                showToast("Failed to register user. Please try again.");
            }
        });
    }

}
