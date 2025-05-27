package com.example.arielsurveysapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.credentials.CredentialManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.arielsurveysapp.model.Student;
import com.example.arielsurveysapp.model.Teacher;
import com.example.arielsurveysapp.model.User;
import com.example.arielsurveysapp.services.AuthenticationService;
import com.example.arielsurveysapp.services.DatabaseService;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDetails extends AppCompatActivity implements View.OnClickListener {

    private TextView tvFirstName, tvLastName, tvGender, tvPhone, tvStudentClass, tvSection;
    private EditText etEmail, etPassword;
    private Button btnSave, btnCancel;
    private LinearLayout studentInfoLayout;

    private String userId;
    private String userType;
    private User currentUser;
    private DatabaseService databaseService;

    AuthenticationService authenticationService;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_details);

        initViews();
        userType=LoginActivity.userType;
        authenticationService=AuthenticationService.getInstance();


        userId=authenticationService.getCurrentUserId();



        databaseService = DatabaseService.getInstance();



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        loadUserData();
    }

    private void initViews() {
        tvFirstName = findViewById(R.id.tvFirstName);
        tvLastName = findViewById(R.id.tvLastName);
        tvGender = findViewById(R.id.tvGender);
        tvPhone = findViewById(R.id.tvPhone);
        tvStudentClass = findViewById(R.id.tvStudentClass);
        tvSection = findViewById(R.id.tvSection);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        studentInfoLayout = findViewById(R.id.studentInfoLayout);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void loadUserData() {

        if (userType.equals("student")) {
            databaseService.getStudent(userId, new DatabaseService.DatabaseCallback<Student>() {
                @Override
                public void onCompleted(Student student) {
                    if (student != null) {
                        currentUser = student;
                        populateUserData(student);
                        studentInfoLayout.setVisibility(View.VISIBLE);
                        tvStudentClass.setText(student.getStudentClass());
                        tvSection.setText(student.getSection());
                    } else {
                        Toast.makeText(UserDetails.this, "לא נמצא מידע על התלמיד", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(UserDetails.this, "שגיאה בטעינת נתוני התלמיד: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        } else {

            databaseService.getTeacher(userId, new DatabaseService.DatabaseCallback<Teacher>() {
                @Override
                public void onCompleted(Teacher teacher) {
                    if (teacher != null) {
                        currentUser = teacher;
                        populateUserData(teacher);
                        studentInfoLayout.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(UserDetails.this, "לא נמצא מידע על המורה", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onFailed(Exception e) {
                    Toast.makeText(UserDetails.this, "שגיאה בטעינת נתוני המורה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }


    private void populateUserData(User user) {
        tvFirstName.setText(user.getfName());
        tvLastName.setText(user.getlName());
        tvGender.setText(user.getGender());
        tvPhone.setText(user.getPhone());

        etEmail.setText(user.getEmail());
        etPassword.setText(user.getPassword());
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {
            saveUserChanges();
        } else if (v == btnCancel) {
            finish();
        }
    }

    private void saveUserChanges() {
        String newEmail = etEmail.getText().toString().trim();
        String newPassword = etPassword.getText().toString().trim();

        if (newEmail.isEmpty()) {
            Toast.makeText(this, "אימייל לא יכול להיות ריק", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.isEmpty()) {
            Toast.makeText(this, "סיסמה לא יכולה להיות ריקה", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "סיסמה חייבת להכיל לפחות 6 תווים", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            Toast.makeText(this, "כתובת אימייל לא תקינה", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser.setEmail(newEmail);
        currentUser.setPassword(newPassword);

        ChangePassword( newEmail);

        if ("student".equals(userType)) {
            databaseService.createNewStudent((Student) currentUser, new DatabaseService.DatabaseCallback<Void>() {
                @Override
        public void onCompleted(Void aVoid) {
                    Toast.makeText(UserDetails.this, "פרטי התלמיד עודכנו בהצלחה", Toast.LENGTH_SHORT).show();

                    finish();
                }

                @Override
                public void onFailed(Exception e) {
                    databaseService.createNewStudent((Student) currentUser, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void aVoid) {
                            Toast.makeText(UserDetails.this, "פרטי התלמיד עודכנו בהצלחה", Toast.LENGTH_SHORT).show();

                            finish();
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(UserDetails.this, "שגיאה בעדכון פרטי התלמיד: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } else {
            databaseService.createNewTeacher((Teacher) currentUser, new DatabaseService.DatabaseCallback<Void>() {
                @Override
                public void onCompleted(Void aVoid) {
                    Toast.makeText(UserDetails.this, "פרטי המורה עודכנו בהצלחה", Toast.LENGTH_SHORT).show();

                    finish();
                }

                @Override
                public void onFailed(Exception e) {
                    databaseService.createNewTeacher((Teacher) currentUser, new DatabaseService.DatabaseCallback<Void>() {
                        @Override
                        public void onCompleted(Void aVoid) {
                            Toast.makeText(UserDetails.this, "פרטי המורה עודכנו בהצלחה", Toast.LENGTH_SHORT).show();

                            finish();
                        }

                        @Override
                        public void onFailed(Exception e) {
                            Toast.makeText(UserDetails.this, "שגיאה בעדכון פרטי המורה: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }
    }





    public void ChangePassword(String email){

        FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(UserDetails.this, "Reset email sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserDetails.this, "Failed to send reset email", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}