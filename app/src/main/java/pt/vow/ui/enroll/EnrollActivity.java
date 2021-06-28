package pt.vow.ui.enroll;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.R;
import pt.vow.ui.VOW;
import pt.vow.ui.getActivities.ActivitiesRegisteredView;
import pt.vow.ui.login.LoggedInUserView;

public class EnrollActivity extends AppCompatActivity {

    private TextView textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private Button enrollButton;
    private EnrollViewModel enrollViewModel;
    private LoggedInUserView user;
    private String[] activityInfo;
    private String activityInfoTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);

        textViewActName = findViewById(R.id.textViewActName);
        textViewActOwner = findViewById(R.id.textViewActOwner);
        textViewAddress = findViewById(R.id.textViewAddress);
        textViewDuration = findViewById(R.id.textViewDuration);
        textViewNumPart = findViewById(R.id.textViewNumParticipants);
        textViewTime = findViewById(R.id.textViewTime);

        enrollButton = findViewById(R.id.enrollButton);

        enrollViewModel = new ViewModelProvider(this, new EnrollViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(EnrollViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activityInfoTitle = (String) getIntent().getSerializableExtra("ActivityInfo");

        activityInfo = activityInfoTitle.split("_");

        textViewActName.setText("Activity Name: " + activityInfo[0]);
        textViewActOwner.setText("Entity: " + activityInfo[1]);
        textViewAddress.setText("Address: "+ activityInfo[2]);
        textViewTime.setText("Time: " + activityInfo[3]);
        textViewNumPart.setText("Number of Participants: " + activityInfo[4]);
        textViewDuration.setText("Duration: " + Integer.parseInt(activityInfo[5])/60 + "h" + Integer.parseInt(activityInfo[5])%60);

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
                Intent intentPop = new Intent(EnrollActivity.this, Pop.class);
                intentPop.putExtra("title", activityInfo[0]);
                intentPop.putExtra("location", activityInfo[2]);
                intentPop.putExtra("time", activityInfo[3]);
                intentPop.putExtra("duration", Integer.parseInt(activityInfo[5])/60 + ":" + Integer.parseInt(activityInfo[5])%60);
                startActivity(intentPop);
                enrollViewModel.enrollInActivity(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6]);
                Toast.makeText(getApplicationContext(), "Joined Activity!", Toast.LENGTH_SHORT).show();
                enrollButton.setEnabled(false);
            }
        });
    }
}
