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
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
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
        holder.activityName.setText(holder.itemView.getContext().getString(R.string.prompt_name) +" "+ activityList.get(position).getName());
        holder.owner.setText(holder.itemView.getContext().getString(R.string.organization) +" "+ activityList.get(position).getOwner());
        holder.time.setText(holder.itemView.getContext().getString(R.string.date)+" "+ activityList.get(position).getTime());
        holder.duration.setText(holder.itemView.getContext().getString(R.string.duration)+" " + activityList.get(position).getDurationInMinutes() + " minutes");

        if (activityList.get(position).getImage() != null) {
            holder.activityImage.setVisibility(View.VISIBLE);
            holder.owner.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
            holder.duration.setVisibility(View.GONE);
            byte[] img = activityList.get(position).getImage().getImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            holder.activityImage.setImageBitmap(bitmap);
        }

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView activityName, owner, time, duration;
        ImageView activityImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.textViewActivityName);
            owner = itemView.findViewById(R.id.textViewOwner);
            time = itemView.findViewById(R.id.textViewTime);
            duration = itemView.findViewById(R.id.textViewDuration);
            activityImage = itemView.findViewById(R.id.activityImage);
        }
    }
}
