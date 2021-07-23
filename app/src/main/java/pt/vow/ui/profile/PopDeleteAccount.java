package pt.vow.ui.profile;

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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.disableUser.DeleteUserResult;
import pt.vow.ui.disableUser.DeleteUserViewModel;
import pt.vow.ui.disableUser.DeleteUserViewModelFactory;
import pt.vow.ui.frontPage.FrontPageActivity;
import pt.vow.ui.image.DeleteImageViewModel;
import pt.vow.ui.image.DeleteImageViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;

public class PopDeleteAccount extends AppCompatActivity {
    private Button buttonYes, buttonNo;
    private TextView textView;
    private DeleteUserViewModel deleteUserViewModel;
    private DeleteImageViewModel deleteImageViewModel;
    private LoggedInUserView user;
    private PopDeleteAccount mActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        mActivity = this;

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");

        deleteUserViewModel = new ViewModelProvider(this, new DeleteUserViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(DeleteUserViewModel.class);
        deleteImageViewModel = new ViewModelProvider(this, new DeleteImageViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(DeleteImageViewModel.class);

        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        textView = findViewById(R.id.textViewPopUp);
        textView.setText(R.string.confirm_delete_user);
        textView.setGravity(Gravity.CENTER);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .25));

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete user account
                deleteUserViewModel.deleteUser(user.getUsername(), user.getTokenID(), user.getUsername());
                deleteUserViewModel.getDeleteUserResult().observe(mActivity, new Observer<DeleteUserResult>() {
                    @Override
                    public void onChanged(DeleteUserResult deleteUserResult) {
                        if (deleteUserResult == null) {
                            return;
                        }
                        if (deleteUserResult.getError() != null) {
                            showActionFailed(deleteUserResult.getError());
                            return;
                        }
                        if (deleteUserResult.getSuccess() != null) {
                            // delete profile image
                            try {
                                deleteImageViewModel.deleteImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), R.string.user_deleted, Toast.LENGTH_SHORT).show();

                            //go back to front page
                            Intent intent = new Intent(mActivity, FrontPageActivity.class);
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
