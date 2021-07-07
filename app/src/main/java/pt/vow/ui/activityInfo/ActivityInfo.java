package pt.vow.ui.activityInfo;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.login.LoginViewModel;
import pt.vow.ui.login.LoginViewModelFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class ActivityInfo extends AppCompatActivity {

    private ActivityInfo mActivity;
    private TextView textViewRating, textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private Button submitBttn;
    private EditText editTextComment;
    private RatingBar ratingBar;
    private LoggedInUserView user;
    private String activityInfoTitle;
    private String[] activityInfo;
    private Activity activityInfoFromNotification;
    private RatingViewModel ratingViewModel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mActivity = this;

        ratingViewModel = new ViewModelProvider(this, new RatingViewModelFactory(((VOW) getApplication()).getExecutorService()))
                        .get(RatingViewModel.class);

        textViewActName = findViewById(R.id.textViewActName2);
        textViewActOwner = findViewById(R.id.textViewActOwner2);
        textViewAddress = findViewById(R.id.textViewAddress2);
        textViewDuration = findViewById(R.id.textViewDuration2);
        textViewNumPart = findViewById(R.id.textViewNumParticipants2);
        textViewTime = findViewById(R.id.textViewTime2);
        ratingBar = findViewById(R.id.rating_bar);
        submitBttn = findViewById(R.id.submitBttn);
        editTextComment = findViewById(R.id.editTextComment);
        textViewRating = findViewById(R.id.textViewRating);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");

       // activityInfoFromNotification = (Activity) getIntent().getSerializableExtra("Activity");
        activityInfoTitle = (String) getIntent().getSerializableExtra("ActivityInfo");
        activityInfo = activityInfoTitle.split("_");

        textViewActName.setText(Html.fromHtml("<b>" + getResources().getString(R.string.activity_name) +"</b>" + " " + activityInfo[0]));
        textViewActOwner.setText(Html.fromHtml("<b>" +getResources().getString(R.string.organization) +"</b>"+ " " + activityInfo[1]));
        textViewAddress.setText(Html.fromHtml("<b>" +getResources().getString(R.string.address) +"</b>"+ " " + activityInfo[2]));
        textViewTime.setText(Html.fromHtml("<b>" +getResources().getString(R.string.time) +"</b>" +" " + activityInfo[3]));
        textViewNumPart.setText(Html.fromHtml("<b>" +getResources().getString(R.string.number_participants) +"</b>" +" " + activityInfo[4]));
        textViewDuration.setText(Html.fromHtml("<b>" +getResources().getString(R.string.duration) +"</b>" +" " + Integer.parseInt(activityInfo[5]) / 60 + "h" + Integer.parseInt(activityInfo[5]) % 60));

        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingViewModel.setRating(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6], (long) ratingBar.getRating());
            }
        });
    }
}
