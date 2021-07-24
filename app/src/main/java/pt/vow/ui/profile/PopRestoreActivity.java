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

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.disableUser.DeleteUserResult;
import pt.vow.ui.disableUser.DeleteUserViewModel;
import pt.vow.ui.disableUser.DeleteUserViewModelFactory;
import pt.vow.ui.frontPage.FrontPageActivity;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.restoreActivity.RestoreActivityViewModel;
import pt.vow.ui.restoreActivity.RestoreActivityViewModelFactory;

public class PopRestoreActivity extends AppCompatActivity {
    private Button buttonYes, buttonNo;
    private TextView textView;
    private RestoreActivityViewModel restoreActivityViewModel;
    private LoggedInUserView user;
    private Activity activity;
    private PopRestoreActivity mActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        mActivity = this;

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");

        restoreActivityViewModel = new ViewModelProvider(this, new RestoreActivityViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(RestoreActivityViewModel.class);

        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        textView = findViewById(R.id.textViewPopUp);
        textView.setText(R.string.restore_activity);
        textView.setGravity(Gravity.CENTER);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .25));

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //restore activity
                restoreActivityViewModel.restoreActivity(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                restoreActivityViewModel.getRestoreActivityResult().observe(mActivity, restoreActivityResult -> {
                    if (restoreActivityResult == null) {
                        return;
                    }
                    if (restoreActivityResult.getError() != null) {
                        showMessage(restoreActivityResult.getError());
                        return;
                    }
                    if (restoreActivityResult.getSuccess() != null) {
                        showMessage(R.string.activity_restored);
                        finish();
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

    private void showMessage(@StringRes Integer message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}
