package pt.vow.ui.profile;

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
            holder.activityName.setText(holder.itemView.getContext().getString(R.string.prompt_name) +" "+ activityList.get(position).getName());
            holder.owner.setText(holder.itemView.getContext().getString(R.string.organization) +" "+ activityList.get(position).getOwner());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ActivityInfoActivity.class);
                    intent.putExtra("Activity", activityList.get(position));
                    intent.putExtra("UserLogged", user);
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return activityList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView activityName, owner;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                activityName = itemView.findViewById(R.id.textViewActivityName);
                owner = itemView.findViewById(R.id.textViewOwner);
            }
        }
}
