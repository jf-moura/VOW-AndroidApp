package pt.vow.ui.feed;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Activity> activityList;

    public RecyclerViewAdapter(Context context, List<Activity> activityList) {
        this.context = context;
        this.activityList = activityList;
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
        holder.address.setText("Location: " + activityList.get(position).getAddress());
        holder.time.setText("Date: " + activityList.get(position).getTime());
        holder.duration.setText("Duration: " + activityList.get(position).getDurationInMinutes() + " minutes");
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView activityName, owner, address, time, duration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.textViewActivityName);
            owner = itemView.findViewById(R.id.textViewOwner);
            address = itemView.findViewById(R.id.textViewAddress);
            time = itemView.findViewById(R.id.textViewTime);
            duration = itemView.findViewById(R.id.textViewDuration);
        }
    }
}
