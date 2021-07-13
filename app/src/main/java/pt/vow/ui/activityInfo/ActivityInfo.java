package pt.vow.ui.activityInfo;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.feed.GetActivitiesResult;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.login.LoginViewModel;
import pt.vow.ui.login.LoginViewModelFactory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import javax.annotation.Nullable;

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
    private GetRatingViewModel getRatingViewModel;
    private ActivityParticipantsViewModel actParticipantsViewModel;
    private double totalRate;
    private String rate;
    private Observer<GetRatingResult> rateObs;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mActivity = this;

        ratingViewModel = new ViewModelProvider(this, new RatingViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(RatingViewModel.class);
        getRatingViewModel = new ViewModelProvider(this, new GetRatingViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetRatingViewModel.class);
        actParticipantsViewModel = new ViewModelProvider(this, new ActivityParticipantsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ActivityParticipantsViewModel.class);
        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");

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


        // activityInfoFromNotification = (Activity) getIntent().getSerializableExtra("Activity");
        activityInfoTitle = (String) getIntent().getSerializableExtra("ActivityInfo");
        activityInfo = activityInfoTitle.split("_");

        getRatingViewModel.getRating(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6]);

        getRatingViewModel.getRatingResult().observeForever(rateObs = new Observer<GetRatingResult>() {
            @Override
            public void onChanged(@Nullable GetRatingResult getRatingResult) {
                if (getRatingResult == null) {
                    totalRate = 0;
                    return;
                }
                if (getRatingResult.getError() != null) {
                    return;
                }
                if (getRatingResult.getSuccess() != null) {
                    rate = getRatingResult.getSuccess().getRating();
                    //activityRatingSum/activityRatingCounter
                    double activityRatingSum = Integer.parseInt(getRatingResult.getSuccess().getActivityRatingSum());
                    double activityRatingCounter = Integer.parseInt(getRatingResult.getSuccess().getActivityRatingCounter());
                    if (activityRatingCounter != 0) {
                        totalRate = activityRatingSum / activityRatingCounter;
                    }
                    if (rate != null) {
                        submitBttn.setVisibility(View.GONE);
                        ratingBar.setRating((float) Integer.parseInt(rate));
                    }

                }
                textViewRating.setText(Html.fromHtml("<b>" + getResources().getString(R.string.rating) + "</b> " + totalRate + "/5.0"));
            }
        });

        actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6]);
        actParticipantsViewModel.getParticipantsResult().observeForever(new Observer<ActivityParticipantsResult>() {
            @Override
            public void onChanged(ActivityParticipantsResult activityParticipantsResult) {
                if (activityParticipantsResult == null) {
                    return;
                }
                if (activityParticipantsResult.getError() != null) {
                    return;
                }
                if (activityParticipantsResult.getSuccess() != null) {
                    List<String> participants = activityParticipantsResult.getSuccess().getParticipants();
                    textViewNumPart.setText(Html.fromHtml("<b>" + getResources().getString(R.string.number_participants) + " </b>" + participants.size() + "/" + activityInfo[4]));
                }
            }
        });

        textViewActName.setText(Html.fromHtml("<b>" + getResources().getString(R.string.activity_name) + "</b>" + " " + activityInfo[0]));
        textViewActOwner.setText(Html.fromHtml("<b>" + getResources().getString(R.string.organization) + "</b>" + " " + activityInfo[1]));
        textViewAddress.setText(Html.fromHtml("<b>" + getResources().getString(R.string.address) + "</b>" + " " + activityInfo[2]));
        textViewTime.setText(Html.fromHtml("<b>" + getResources().getString(R.string.time) + "</b>" + " " + activityInfo[3]));
        textViewNumPart.setText(Html.fromHtml("<b>" + getResources().getString(R.string.number_participants) + "</b>" + " " + activityInfo[4]));
        textViewDuration.setText(Html.fromHtml("<b>" + getResources().getString(R.string.duration) + "</b>" + " " + Integer.parseInt(activityInfo[5]) / 60 + "h" + Integer.parseInt(activityInfo[5]) % 60));




        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingViewModel.setRating(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6], (long) ratingBar.getRating());
                Toast.makeText(getApplicationContext(), R.string.commit_rating, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
