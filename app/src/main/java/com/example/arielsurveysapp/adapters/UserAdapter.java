package com.example.arielsurveysapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.arielsurveysapp.R;
import com.example.arielsurveysapp.model.Teacher;
import com.example.arielsurveysapp.model.User;
import java.lang.reflect.Field;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private final List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        holder.tvFname.setText(user.getfName());
        holder.tvLname.setText(user.getlName());

        // Check if the user has a "subject" field (indicating a Teacher)
        boolean isTeacher = user instanceof Teacher;
        holder.tvRole.setText(isTeacher ? "Teacher" : "Student");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvFname, tvLname, tvRole;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFname = itemView.findViewById(R.id.tvFname);
            tvLname = itemView.findViewById(R.id.tvLname);
            tvRole = itemView.findViewById(R.id.tvRole);
        }
    }
}
