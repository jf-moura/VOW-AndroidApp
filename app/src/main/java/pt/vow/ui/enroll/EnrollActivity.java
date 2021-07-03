package pt.vow.ui.enroll;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.getActivities.ActivitiesRegisteredView;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.GetActivitiesByUserResult;
import pt.vow.ui.profile.GetActivitiesByUserViewModel;
import pt.vow.ui.profile.GetActivitiesByUserViewModelFactory;
import pt.vow.ui.profile.ProfileRecyclerViewAdapter;

public class EnrollActivity extends AppCompatActivity {

    private TextView textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private Button enrollButton;
    private EnrollViewModel enrollViewModel;
    private CancelEnrollViewModel cancelEnrollViewModel;
    private LoggedInUserView user;
    private String[] activityInfo;
    private String activityInfoTitle;
    private List<Activity> activitiesList;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private Activity aux;
    private EnrollActivity mActivity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        mActivity = this;

        textViewActName = findViewById(R.id.textViewActName);
        textViewActOwner = findViewById(R.id.textViewActOwner);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewDuration = findViewById(R.id.textViewDuration);
        textViewNumPart = findViewById(R.id.textViewNumParticipants);
        textViewTime = findViewById(R.id.textViewTime);

        enrollButton = findViewById(R.id.enrollButton);

        enrollViewModel = new ViewModelProvider(this, new EnrollViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(EnrollViewModel.class);

        cancelEnrollViewModel = new ViewModelProvider(this, new CancelEnrollViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(CancelEnrollViewModel.class);

        getActivitiesByUserViewModel = new ViewModelProvider(this, new GetActivitiesByUserViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetActivitiesByUserViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activityInfoTitle = (String) getIntent().getSerializableExtra("ActivityInfo");

        activityInfo = activityInfoTitle.split("_");


        textViewActName.setText(getResources().getString(R.string.activity_name) +" "+ activityInfo[0]);
        textViewActOwner.setText(getResources().getString(R.string.organization) +" "+ activityInfo[1]);
        textViewAddress.setText(getResources().getString(R.string.address) +" "+ activityInfo[2]);
        textViewTime.setText(getResources().getString(R.string.time) +" "+ activityInfo[3]);
        textViewNumPart.setText(getResources().getString(R.string.number_participants) +" "+ activityInfo[4]);
        textViewDuration.setText(getResources().getString(R.string.duration) +" "+ Integer.parseInt(activityInfo[5]) / 60 + "h" + Integer.parseInt(activityInfo[5]) % 60);

        getActivitiesByUserViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));
        getActivitiesByUserViewModel.getActivitiesResult().observeForever(new Observer<GetActivitiesByUserResult>() {
            @Override
            public void onChanged(@Nullable GetActivitiesByUserResult getActivitiesResult) {
                if (getActivitiesResult == null) {
                    return;
                }
                if (getActivitiesResult.getError() != null) {
                    showGetActivitiesFailed(getActivitiesResult.getError());
                }
                if (getActivitiesResult.getSuccess() != null) {
                    getActivitiesByUserViewModel.getActivitiesList().observe(mActivity, list -> {
                        activitiesList = list;
                    });
                    if (activitiesList != null) {
                        for (Activity a : activitiesList) {
                            if (a.getId().equals(activityInfo[6])) {
                                aux = a;
                            }
                        }
                    }
                    if (aux != null) { //it means that user already joined the activity
                        enrollButton.setText(getResources().getString(R.string.unjoin));
                    }
                    setResult(android.app.Activity.RESULT_OK);
                }
            }
        });

        enrollViewModel.getEnrollResult().observe(this, new Observer<EnrollResult>() {

            @Override
            public void onChanged(EnrollResult enrollResult) {
                if (enrollResult == null) {
                    return;
                }
                if (enrollResult.getError() != null) {
                    showEnrollFailed(enrollResult.getError());
                }
                if (enrollResult.getSuccess() != null) {
                    // updateUiWithActivities(enrollResult.getSuccess());
                    setResult(android.app.Activity.RESULT_OK);
                    // getActivity().finish();
                }
                //Complete and destroy login activity once successful
                //finish();
            }

            private void showEnrollFailed(@StringRes Integer error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();

            }
        });

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (enrollButton.getText().equals(getResources().getString(R.string.join))) {
                    enrollViewModel.enrollInActivity(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6]);
                    Toast.makeText(getApplicationContext(), "Joined Activity!", Toast.LENGTH_SHORT).show();
                    enrollButton.setText(getResources().getString(R.string.unjoin));
                    //enrollButton.setEnabled(false);
                    // TODO: Falta ver os erros
                    Intent intentPop = new Intent(EnrollActivity.this, Pop.class);
                    intentPop.putExtra("title", activityInfo[0]);
                    intentPop.putExtra("location", activityInfo[2]);
                    intentPop.putExtra("time", activityInfo[3]);
                    intentPop.putExtra("duration", Integer.parseInt(activityInfo[5]) / 60 + ":" + Integer.parseInt(activityInfo[5]) % 60);
                    startActivity(intentPop);
                }
                //if a user is already in join and user wants to cancel
                else {
                    cancelEnrollViewModel.cancelEnrollInActivity(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6]);
                    enrollButton.setText(getResources().getString(R.string.join));
                    Toast.makeText(getApplicationContext(), "Unjoined Activity!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(this.getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}
