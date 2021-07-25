package pt.vow.ui.feed;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.data.model.Commentary;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModel;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModelFactory;
import pt.vow.ui.enroll.EnrollActivity;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.ActivitiesByUserView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Activity> activityList;
    ActivitiesByUserView enrolledActivities;
    LoggedInUserView user;


    public RecyclerViewAdapter(Context context, List<Activity> activityList, LoggedInUserView user, ActivitiesByUserView enrolledActivities) {
        this.context = context;
        this.activityList = activityList;
        this.user = user;
        this.enrolledActivities = enrolledActivities;
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
        if (activityList.get(position).getParticipants() != null && String.valueOf(activityList.get(position).getParticipants().size()).equals(activityList.get(position).getParticipantNum())) {
            holder.constraintLayoutFeed.setBackgroundResource(R.drawable.bg_activity_not_available);
        }

        holder.activityName.setText(activityList.get(position).getName());

        String finalD = showTime(activityList.get(position).getTime());
        holder.owner.setText(holder.itemView.getContext().getString(R.string.organization) + " " + activityList.get(position).getOwner());
        holder.time.setText(holder.itemView.getContext().getString(R.string.date) + " " + finalD);
        holder.duration.setText(holder.itemView.getContext().getString(R.string.duration) + " " + activityList.get(position).getDurationInMinutes() + " minutes");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EnrollActivity.class);
                intent.putExtra("UserLogged", user);
                intent.putExtra("Activity", activityList.get(position));
                intent.putExtra("EnrolledActivities", enrolledActivities);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView activityName, owner, time, duration;
        ImageView activityImage;
        ConstraintLayout constraintLayoutFeed;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.textViewActivityName);
            owner = itemView.findViewById(R.id.textViewOwner);
            time = itemView.findViewById(R.id.textViewTime);
            duration = itemView.findViewById(R.id.textViewDuration);
            activityImage = itemView.findViewById(R.id.activityImage);
            constraintLayoutFeed = itemView.findViewById(R.id.constraintLayoutFeed);
        }
    }

    private String showTime(String time) {
        String[] dateTime = time.split(" ");
        String[] hours = dateTime[3].split(":");

        String h = hours[0] + ":" + hours[1] + " " + dateTime[4];
        String d = dateTime[1].split(",")[0] + "/" + dateTime[0] + "/" + dateTime[2];
        String finalD = d + " " + h;

        return finalD;
    }
}