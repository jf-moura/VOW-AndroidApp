package pt.vow.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.enroll.EnrollActivity;
import pt.vow.ui.login.LoggedInUserView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Activity> activityList;
    LoggedInUserView user;

    public RecyclerViewAdapter(Context context, List<Activity> activityList, LoggedInUserView user) {
        this.context = context;
        this.activityList = activityList;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_feed_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.activityName.setText("Name: " + activityList.get(position).getName());
        holder.owner.setText("Organization: " + activityList.get(position).getOwner());
        holder.time.setText("Date: " + activityList.get(position).getTime());
        holder.duration.setText("Duration: " + activityList.get(position).getDurationInMinutes() + " minutes");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String activity = activityList.get(position).getName() + "_" + activityList.get(position).getOwner() + "_" + activityList.get(position).getAddress() + "_" + activityList.get(position).getTime() + "_" + activityList.get(position).getParticipantNum() + "_" + activityList.get(position).getDurationInMinutes() + "_" + activityList.get(position).getId();
                Intent intent = new Intent(context, EnrollActivity.class);
                intent.putExtra("UserLogged", user);
                intent.putExtra("ActivityInfo", activity);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView activityName, owner, time, duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.textViewActivityName);
            owner = itemView.findViewById(R.id.textViewOwner);
            time = itemView.findViewById(R.id.textViewTime);
            duration = itemView.findViewById(R.id.textViewDuration);
        }
    }
}
