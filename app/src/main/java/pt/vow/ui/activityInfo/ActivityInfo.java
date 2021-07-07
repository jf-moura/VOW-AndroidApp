package pt.vow.ui.activityInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.login.LoggedInUserView;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityInfo extends AppCompatActivity {

    private ActivityInfo mActivity;
    private TextView textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private Button submitBttn;
    private EditText editTextComment;
    private RatingBar ratingBar;
    private LoggedInUserView user;
    private String activityInfoTitle;
    private String[] activityInfo;
    private Activity activityInfoFromNotification;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mActivity = this;

        textViewActName = findViewById(R.id.textViewActName2);
        textViewActOwner = findViewById(R.id.textViewActOwner2);
        textViewAddress = findViewById(R.id.textViewAddress2);
        textViewDuration = findViewById(R.id.textViewDuration2);
        textViewNumPart = findViewById(R.id.textViewNumParticipants2);
        textViewTime = findViewById(R.id.textViewTime2);
        ratingBar = findViewById(R.id.rating_bar);
        submitBttn = findViewById(R.id.submitBttn);
        editTextComment = findViewById(R.id.editTextComment);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
       // activityInfoFromNotification = (Activity) getIntent().getSerializableExtra("Activity");
        activityInfoTitle = (String) getIntent().getSerializableExtra("ActivityInfo");
        activityInfo = activityInfoTitle.split("_");

        textViewActName.setText(getResources().getString(R.string.activity_name) +" "+ activityInfo[0]);
        textViewActOwner.setText(getResources().getString(R.string.organization)+" " + activityInfo[1]);
        textViewAddress.setText(getResources().getString(R.string.address)+" " + activityInfo[2]);
        textViewTime.setText(getResources().getString(R.string.time)+" " + activityInfo[3]);
        textViewNumPart.setText(getResources().getString(R.string.number_participants)+" " + activityInfo[4]);
        textViewDuration.setText(getResources().getString(R.string.duration)+" " + Integer.parseInt(activityInfo[5]) / 60 + "h" + Integer.parseInt(activityInfo[5]) % 60);

        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rating = String.valueOf(ratingBar.getRating());

            }
        });
    }
}
