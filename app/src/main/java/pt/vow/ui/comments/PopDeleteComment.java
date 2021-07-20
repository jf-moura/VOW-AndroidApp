package pt.vow.ui.comments;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.data.model.Commentary;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityInfoActivity;
import pt.vow.ui.disableActivity.DeleteActivityResult;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.ProfileFragment;

public class PopDeleteComment extends AppCompatActivity {
    private Button buttonYes, buttonNo;
    private TextView textView;
    private DeleteCommentViewModel deleteCommentViewModel;
    private LoggedInUserView user;
    private Activity activity;
    private Commentary comment;
    private PopDeleteComment mActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        mActivity = this;

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");
        comment = (Commentary) getIntent().getSerializableExtra("Comment");

        deleteCommentViewModel = new ViewModelProvider(this, new DeleteCommentViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(DeleteCommentViewModel.class);

        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        textView = findViewById(R.id.textViewPopUp);
        textView.setText(R.string.confirm_delete_comment);
        textView.setGravity(Gravity.CENTER);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .25));

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the commentary
                deleteCommentViewModel.deleteComment(user.getUsername(), user.getTokenID(), comment.getCommentID(), comment.getCommentOwner(), activity.getId(), activity.getOwner());
                deleteCommentViewModel.getDeleteCommentResult().observe(mActivity, new Observer<DeleteCommentResult>() {
                    @Override
                    public void onChanged(DeleteCommentResult deleteCommentResult) {
                        if (deleteCommentResult == null) {
                            return;
                        }
                        if (deleteCommentResult.getError() != null) {
                            showActionFailed(deleteCommentResult.getError());
                            return;
                        }
                        if (deleteCommentResult.getSuccess() != null) {
                            Toast.makeText(getApplicationContext(), R.string.comment_deleted, Toast.LENGTH_SHORT).show();

                            //go back to activity info activity
                            Intent intent = new Intent(mActivity, ActivityInfoActivity.class);
                            intent.putExtra("UserLogged", user);
                            intent.putExtra("Activity", activity);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void showActionFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }
}
