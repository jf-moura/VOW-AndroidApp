package pt.vow.ui.activityInfo;

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
import pt.vow.ui.VOW;
import pt.vow.ui.disableActivity.DeleteActivityResult;
import pt.vow.ui.disableActivity.DeleteActivityViewModel;
import pt.vow.ui.disableActivity.DeleteActivityViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.FutureActivitiesFragment;
import pt.vow.ui.profile.ProfileFragment;

public class PopDelete extends AppCompatActivity {
    private Button buttonYes, buttonNo;
    private TextView textView;
    private DeleteActivityViewModel deleteActivityViewModel;
    private LoggedInUserView user;
    private Activity activity;
    private PopDelete mActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);
        mActivity = this;
        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");

        deleteActivityViewModel = new ViewModelProvider(this, new DeleteActivityViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(DeleteActivityViewModel.class);

        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        textView = findViewById(R.id.textView5);
        textView.setText(R.string.confirm_delete);
        textView.setGravity(Gravity.CENTER);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .25));

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the activity
                deleteActivityViewModel.getDeleteActivityResult().observe(mActivity, new Observer<DeleteActivityResult>() {
                    @Override
                    public void onChanged(DeleteActivityResult deleteActivityResult) {
                        if (deleteActivityResult == null) {
                            return;
                        }
                        if (deleteActivityResult.getError() != null) {
                            showActionFailed(deleteActivityResult.getError());
                            return;
                        }
                        if (deleteActivityResult.getSuccess() != null) {
                            deleteActivityViewModel.deleteActivity(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                            Toast.makeText(getApplicationContext(), R.string.activity_deleted, Toast.LENGTH_SHORT).show();

                            //go back to profile fragment
                            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            Fragment fragment = new ProfileFragment();
                            fragmentTransaction.replace(R.id.popWindow, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
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