package pt.vow.ui.podium;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.UserInfo;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.ShowProfileActivity;

public class PodiumRecyclerViewAdapter extends RecyclerView.Adapter<PodiumRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<UserInfo> userList;
    LoggedInUserView user;

    public PodiumRecyclerViewAdapter(Context context, List<UserInfo> userList, LoggedInUserView user) {
        this.context = context;
        this.userList = userList;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_podium_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (userList.get(position).getUsername().equals(user.getUsername())) {
            holder.constraintLayout.setBackgroundResource(R.drawable.bg_podium);
            holder.userName.setTextColor(Color.WHITE);
            holder.textViewPoints.setTextColor(Color.WHITE);
        }
        holder.textViewPosition.setText(String.valueOf(position + 1));
        holder.userName.setText(userList.get(position).getName());
        //holder.textViewPoints.setText(holder.itemView.getContext().getString(R.string.organization) +" "+ activityList.get(position).getOwner());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!userList.get(position).getUsername().equals(user.getUsername())) {
                    Intent intent = new Intent(context, ShowProfileActivity.class);
                    intent.putExtra("UserLogged", user);
                    intent.putExtra("UserShown", userList.get(position).getUsername());
                    intent.putExtra("UserShownVisibility", userList.get(position).getVisibility());
                    // intent.putExtra("UserShownToken", userList.get(position));
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView userName, textViewPoints, textViewPosition;
        ImageView profileImage;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.textViewUserName);
            textViewPoints = itemView.findViewById(R.id.textViewPoints);
            profileImage = itemView.findViewById(R.id.profileImagePodium);
            constraintLayout = itemView.findViewById(R.id.constraintLayout);
            textViewPosition = itemView.findViewById(R.id.textViewPosition);
        }
    }
}


