package pt.vow.ui.activityInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class ActivityInfoActivity extends AppCompatActivity {

    private ActivityInfoActivity mActivity;
    private TextView textViewRating, textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private EditText editTextDuration, editTextNumPart, editTextTime, editTextActName, editTextActOwner, editTextAddress;
    private Button submitBttn, saveUpdateBttn;
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
    private ImageButton deleteActBttn;

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

        editTextActName = findViewById(R.id.editTextActName);
        editTextActOwner = findViewById(R.id.editTextOrganization);
        editTextAddress = findViewById(R.id.editTextAddress2);
        editTextDuration = findViewById(R.id.editTextDur);
        editTextNumPart = findViewById(R.id.editTextPartNum);
        editTextTime = findViewById(R.id.editTextTime2);

        ratingBar = findViewById(R.id.rating_bar);
        submitBttn = findViewById(R.id.submitBttn);
        saveUpdateBttn = findViewById(R.id.submitChangesBttn);
        editTextComment = findViewById(R.id.editTextComment);
        textViewRating = findViewById(R.id.textViewRating);

        deleteActBttn = findViewById(R.id.deleteActBttn);


        // activityInfoFromNotification = (Activity) getIntent().getSerializableExtra("Activity");
        activityInfoTitle = (String) getIntent().getSerializableExtra("ActivityInfo");
        activityInfo = activityInfoTitle.split("_");

        //if the user is not the owner of the activity
        if (!user.getTokenID().equals(activityInfo[1])) {
            editTextActName.setFocusable(false);
            editTextActName.setClickable(false);
            editTextActName.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            editTextActOwner.setFocusable(false);
            editTextActOwner.setClickable(false);
            editTextActOwner.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            editTextAddress.setFocusable(false);
            editTextAddress.setClickable(false);
            editTextAddress.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            editTextNumPart.setFocusable(false);
            editTextNumPart.setClickable(false);
            editTextNumPart.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            editTextTime.setFocusable(false);
            editTextTime.setClickable(false);
            editTextTime.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            editTextDuration.setFocusable(false);
            editTextDuration.setClickable(false);
            editTextDuration.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
            saveUpdateBttn.setVisibility(View.GONE);
        }

        getRatingViewModel.getRating(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6]);

        getRatingViewModel.getRatingResult().observeForever(rateObs = new Observer<GetRatingResult>() {
            @Override
            public void onChanged(@Nullable GetRatingResult getRatingResult) {
                if (getRatingResult == null) {
                    totalRate = 0.0;
                    return;
                }
                if (getRatingResult.getError() != null) {
                    return;
                }
                if (getRatingResult.getSuccess() != null) {
                    rate = getRatingResult.getSuccess().getRating();
                    //activityRatingSum/activityRatingCounter
                    double activityRatingSum = Double.parseDouble(getRatingResult.getSuccess().getActivityRatingSum());
                    double activityRatingCounter = Double.parseDouble(getRatingResult.getSuccess().getActivityRatingCounter());
                    if (activityRatingCounter != 0.0) {
                        totalRate = activityRatingSum / activityRatingCounter;
                    }
                    if (rate != null) {
                        submitBttn.setVisibility(View.GONE);
                        ratingBar.setRating((float) Integer.parseInt(rate));
                        ratingBar.setFocusable(false);
                        ratingBar.setIsIndicator(true);
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
                    editTextNumPart.setText(participants.size() + "/" + activityInfo[4]);
                }
            }
        });

        editTextActName.setText(" " + activityInfo[0]);
        editTextActOwner.setText(" " + activityInfo[1]);
        editTextAddress.setText( " " + activityInfo[2]);
        editTextTime.setText(" " + activityInfo[3]);
       // editTextNumPart.setText(" " + activityInfo[4]);
        editTextDuration.setText(" " + Integer.parseInt(activityInfo[5]) / 60 + "h" + Integer.parseInt(activityInfo[5]) % 60);
        textViewRating.setText(Html.fromHtml("<b>" + getResources().getString(R.string.rating) + "</b> " + totalRate + "/5.0"));

        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingViewModel.setRating(user.getUsername(), user.getTokenID(), activityInfo[1], activityInfo[6], (long) ratingBar.getRating());
                Toast.makeText(getApplicationContext(), R.string.commit_rating, Toast.LENGTH_SHORT).show();
            }
        });

        editTextAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.ADDRESS, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(mActivity);
                startActivityForResult(intent, 100);
            }
        });

        deleteActBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPop = new Intent(ActivityInfoActivity.this, PopDelete.class);
                startActivity(intentPop);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK) {
            Place pl = Autocomplete.getPlaceFromIntent(data);
            editTextAddress.setText(pl.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
        }
    }
}
