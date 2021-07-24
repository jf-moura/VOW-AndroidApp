package pt.vow.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import pt.vow.data.model.Activity;
import pt.vow.ui.activityInfo.ActivityInfoActivity;
import pt.vow.ui.login.LoggedInUserView;

public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Activity> activityList;
    LoggedInUserView user;

    public ProfileRecyclerViewAdapter(Context context, List<Activity> activityList, LoggedInUserView user) {
        this.context = context;
        this.activityList = activityList;
        this.user = user;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.fragment_profile_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // activity was removed by the user
        if (!activityList.get(position).getStatus()) {
            holder.constraintLayoutProfile.setBackgroundResource(R.drawable.bg_activity_not_available);
            holder.activityName.setText(R.string.activity_not_active);
            holder.owner.setText(holder.itemView.getContext().getString(R.string.prompt_name) + " " + activityList.get(position).getName());
            holder.activityImage.setVisibility(View.GONE);
        } else {
            holder.activityName.setText(holder.itemView.getContext().getString(R.string.prompt_name) + " " + activityList.get(position).getName());
            holder.owner.setText(holder.itemView.getContext().getString(R.string.organization) + " " + activityList.get(position).getOwner());

            if (activityList.get(position).getImage() != null) {
                byte[] img = activityList.get(position).getImage().getImageBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                holder.activityImage.setVisibility(View.VISIBLE);
                holder.activityImage.setImageBitmap(bitmap);
            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activityList.get(position).getStatus()) {
                    Intent intent = new Intent(context, ActivityInfoActivity.class);
                    intent.putExtra("Activity", activityList.get(position));
                    intent.putExtra("UserLogged", user);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, PopRestoreActivity.class);
                    intent.putExtra("Activity", activityList.get(position));
                    intent.putExtra("UserLogged", user);
                    context.startActivity(intent);
                }
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

        TextView activityName, owner;
        ImageView activityImage;
        ConstraintLayout constraintLayoutProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            activityName = itemView.findViewById(R.id.textViewActivityName);
            owner = itemView.findViewById(R.id.textViewOwner);
            activityImage = itemView.findViewById(R.id.activityImageProfile);
            constraintLayoutProfile = itemView.findViewById(R.id.constraintLayoutProfile);
        }
    }
}
