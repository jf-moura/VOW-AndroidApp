package pt.vow.ui.comments;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.data.model.Commentary;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityInfoActivity;
import pt.vow.ui.activityInfo.GetRatingViewModel;
import pt.vow.ui.activityInfo.GetRatingViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder> {
    Context context;
    List<Commentary> commentList;
    LoggedInUserView user;
    Activity activity;
    ActivityInfoActivity mActivity;

    public CommentsRecyclerViewAdapter(Context context, ActivityInfoActivity mActivity, List<Commentary> commentList, LoggedInUserView user, Activity activity) {
        this.context = context;
        this.commentList = commentList;
        this.user = user;
        this.activity = activity;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.activity_info_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewUserName.setText(commentList.get(position).getCommentOwner());
        holder.textViewComment.setText(commentList.get(position).getComment());
        holder.textViewTimestamp.setText(commentList.get(position).getLastModificationTime());

        if (commentList.get(position).getCommentOwner().equals(user.getUsername())) {
            holder.editBttn.setVisibility(View.VISIBLE);
            holder.trashBttn.setVisibility(View.VISIBLE);


            UpdateCommentViewModel updateCommentViewModel = new ViewModelProvider(mActivity, new UpdateCommentViewModelFactory(((VOW) mActivity.getApplication()).getExecutorService()))
                    .get(UpdateCommentViewModel.class);

            holder.trashBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, PopDeleteComment.class);
                    intent.putExtra("UserLogged", user);
                    intent.putExtra("Activity", activity);
                    intent.putExtra("Comment", commentList.get(position));
                    mActivity.startActivity(intent);
                }
            });

            holder.editBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.editTextComment.setText(holder.textViewComment.getText().toString());
                    holder.textViewComment.setVisibility(View.GONE);
                    holder.editTextComment.setVisibility(View.VISIBLE);
                    holder.editBttn.setVisibility(View.GONE);
                    holder.confirmBttn.setVisibility(View.VISIBLE);
                }
            });

            holder.confirmBttn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateCommentViewModel.updateComment(user.getUsername(), user.getTokenID(),
                            commentList.get(position).getCommentID(), activity.getOwner(), activity.getId(),
                            holder.editTextComment.getText().toString());
                    holder.confirmBttn.setEnabled(true);
                }
            });

            updateCommentViewModel.getUpdateCommentResult().observe(mActivity, new Observer<UpdateCommentResult>() {
                @Override
                public void onChanged(UpdateCommentResult updateCommentResult) {
                    if (updateCommentResult == null) {
                        return;
                    }
                    if (updateCommentResult.getError() != null) {
                        Toast.makeText(context, R.string.register_comment_failed, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (updateCommentResult.getSuccess() != null) {
                        holder.textViewComment.setText(holder.editTextComment.getText().toString());
                        holder.editTextComment.setVisibility(View.GONE);
                        holder.textViewComment.setVisibility(View.VISIBLE);
                        holder.confirmBttn.setVisibility(View.GONE);
                        holder.editBttn.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewUserName, textViewComment, textViewTimestamp;
        EditText editTextComment;
        ImageButton editBttn, confirmBttn, trashBttn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewComment = itemView.findViewById(R.id.textViewComment);
            editTextComment = itemView.findViewById(R.id.editTextComment2);
            editBttn = itemView.findViewById(R.id.editBttn);
            confirmBttn = itemView.findViewById(R.id.confirmCommentBttn);
            trashBttn = itemView.findViewById(R.id.trashBttn);
            textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        }
    }
}
