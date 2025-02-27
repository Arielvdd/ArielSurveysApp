package com.example.arielsurveysapp;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arielsurveysapp.adapters.UserAdapter;
import com.example.arielsurveysapp.model.User;
import com.example.arielsurveysapp.services.DatabaseService;
import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private static final String TAG = "UsersActivity";
    private DatabaseService databaseService;
    private ArrayList<User> userList;
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        initViews();
        loadUsers();
    }

    private void initViews() {
        databaseService = DatabaseService.getInstance();
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);
        recyclerViewUsers.setAdapter(userAdapter);
    }

    private void loadUsers() {
        databaseService.getAllUsers(new DatabaseService.DatabaseCallback<List<User>>() {
            @Override
            public void onCompleted(List<User> users) {
                Log.d(TAG, "Users fetched: " + users.size());
                userList.clear();
                userList.addAll(users);
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailed(Exception e) {
                Log.e(TAG, "Error fetching users", e);
            }
        });
    }
}
