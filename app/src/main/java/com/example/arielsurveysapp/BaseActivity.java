package com.example.arielsurveysapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.arielsurveysapp.services.AuthenticationService;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.arielsurveysapp.databinding.ActivityBaseBinding;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        if (id == R.id.action_my_details) {
            intent = new Intent(this, UserDetails.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.action_my_surveys) {
            intent = new Intent(this,AssignedSurveysActivity.class);
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