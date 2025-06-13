package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.arielsurveysapp.services.AuthenticationService;

public class TeacherBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.action_create_survey) {
            intent = new Intent(this, CreateSurveyActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_view_surveys) {
            intent = new Intent(this, SurveysActivity.class);
            intent.putExtra("status", "close");
            startActivity(intent);
            return true;

        } else if (id == R.id.action_publish_surveys) {
            intent = new Intent(this, SurveysActivity.class);
            intent.putExtra("status", "open");
            startActivity(intent);
            return true;

        } else if (id == R.id.action_view_users) {
            intent = new Intent(this, UsersActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_logout) {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
            AuthenticationService.getInstance().signOut();
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}