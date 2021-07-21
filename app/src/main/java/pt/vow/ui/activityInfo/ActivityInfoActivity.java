package pt.vow.ui.activityInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.data.model.Commentary;
import pt.vow.ui.VOW;
import pt.vow.ui.comments.CommentsRecyclerViewAdapter;
import pt.vow.ui.comments.GetActCommentsResult;
import pt.vow.ui.comments.GetActCommentsViewModel;
import pt.vow.ui.comments.GetActCommentsViewModelFactory;
import pt.vow.ui.comments.RegisterCommentResult;
import pt.vow.ui.comments.RegisterCommentViewModel;
import pt.vow.ui.comments.RegisterCommentViewModelFactory;
import pt.vow.ui.confimParticipants.ConfirmParticipantsActivity;
import pt.vow.ui.disableActivity.DeleteActivityViewModel;
import pt.vow.ui.disableActivity.DeleteActivityViewModelFactory;
import pt.vow.ui.enroll.EnrollResult;
import pt.vow.ui.enroll.EnrollViewModel;
import pt.vow.ui.enroll.EnrollViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.mainPage.Image;
import pt.vow.ui.profile.GetActivitiesByUserResult;
import pt.vow.ui.profile.ProfileRecyclerViewAdapter;


import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ActivityInfoActivity extends AppCompatActivity {

    private ActivityInfoActivity mActivity;
    private TextView textViewConfirmPart, textViewRating, textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private EditText editTextDuration, editTextNumPart, editTextTime, editTextActName, editTextActOwner, editTextAddress;
    private Button submitBttn, saveUpdateBttn, postCommentBttn, checkPartBttn;
    private EditText editTextComment;
    private RatingBar ratingBar;
    private ImageView activityImage;

    private LoggedInUserView user;
    private Activity activity;
    private GetRatingViewModel getRatingViewModel;
    private SetRatingViewModel setRatingViewModel;
    private DeleteActivityViewModel deleteActivityViewModel;
    private ActivityParticipantsViewModel actParticipantsViewModel;
    private double totalRate;
    //private long rate;
    private Observer<GetRatingResult> rateObs;
    private ImageButton deleteActBttn;
    private ImageView imageType;
    private RegisterCommentViewModel registerCommentViewModel;
    private GetActCommentsViewModel getActCommentsViewModel;

    private Observer<GetActCommentsResult> actCommentsObs;
    private List<Commentary> commentaryList;
    private RecyclerView actCommentsRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        mActivity = this;

        getRatingViewModel = new ViewModelProvider(this, new GetRatingViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetRatingViewModel.class);
        setRatingViewModel = new ViewModelProvider(this, new SetRatingViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(SetRatingViewModel.class);
        actParticipantsViewModel = new ViewModelProvider(this, new ActivityParticipantsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ActivityParticipantsViewModel.class);
        deleteActivityViewModel = new ViewModelProvider(this, new DeleteActivityViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(DeleteActivityViewModel.class);
        registerCommentViewModel = new ViewModelProvider(this, new RegisterCommentViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(RegisterCommentViewModel.class);
        getActCommentsViewModel = new ViewModelProvider(this, new GetActCommentsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetActCommentsViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");

        getSupportActionBar().setTitle(activity.getName());

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
        imageType = findViewById(R.id.imageViewType2);
        activityImage = findViewById(R.id.activityImageInfo);
        postCommentBttn = findViewById(R.id.buttonPostComment);
        actCommentsRecyclerView = findViewById(R.id.comments_recycler_view);
//        checkPartBttn = findViewById(R.id.checkPartBttn);
        textViewConfirmPart = findViewById(R.id.textViewConfirmPart);

        // showImageType();

        //if the user is not the owner of the activity
        if (!user.getUsername().equals(activity.getOwner()))
            hideOwnerFunctionalities();

        Calendar currentTime = Calendar.getInstance();
        String[] dateTime = activity.getTime().split(" ");
        String[] hours = dateTime[3].split(":");

        Calendar beginTime = Calendar.getInstance();

        if (dateTime[4].equals("PM"))
            beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length() - 1)), Integer.valueOf(hours[0]) + 12, Integer.valueOf(hours[1]));
        else
            beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length() - 1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));

        long startMillis = beginTime.getTimeInMillis();

        if (activity.getOwner().equals(user.getUsername())) {
            textViewConfirmPart.setVisibility(View.VISIBLE);
        }
        if (startMillis >= currentTime.getTimeInMillis()) {
            textViewRating.setVisibility(View.INVISIBLE);
        }
        // Activity hasn't occured or the user is the owner of the activity
        if (startMillis >= currentTime.getTimeInMillis() || activity.getOwner().equals(user.getUsername())) {
            ratingBar.setVisibility(View.INVISIBLE);
            submitBttn.setVisibility(View.INVISIBLE);
        } else {
            getRatingViewModel.getRating(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());

            getRatingViewModel.rating().observe(this, rate -> {
                double activityRatingSum = Double.parseDouble(rate.getActivityRatingSum());
                double activityRatingCounter = Double.parseDouble(rate.getActivityRatingCounter());
                if (activityRatingCounter != 0.0) {
                    totalRate = activityRatingSum / activityRatingCounter;
                }
                if (rate.getUsername().equals(user.getUsername()) && Integer.parseInt(rate.getRating()) > 0) {
                    submitBttn.setVisibility(View.GONE);
                    ratingBar.setRating(Float.parseFloat(rate.getRating()));
                    ratingBar.setFocusable(false);
                    ratingBar.setIsIndicator(true);
                    getRatingViewModel.getRating(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());

                }
                textViewRating.setText(Html.fromHtml("<b>" + getResources().getString(R.string.rating) + "</b> " + totalRate + "/5.0"));
            });
        }


        actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
        actParticipantsViewModel.getParticipantsList().observe(this, participants -> {
            editTextNumPart.setText(participants.size() + "/" + activity.getParticipantNum());
        });

        editTextActName.setText(" " + activity.getName());
        editTextActOwner.setText(" " + activity.getOwner());
        editTextAddress.setText(" " + activity.getAddress());
        editTextTime.setText(" " + activity.getTime());
        editTextDuration.setText(" " + Integer.parseInt(activity.getDurationInMinutes()) / 60 + "h" + Integer.parseInt(activity.getDurationInMinutes()) % 60);
        textViewRating.setText(Html.fromHtml("<b>" + getResources().getString(R.string.rating) + "</b> " + totalRate + "/5.0"));

        submitBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRatingViewModel.setRating(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId(), (long) ratingBar.getRating());
            }
        });

        setRatingViewModel.getRatingResult().observe(this, new Observer<SetRatingResult>() {
            @Override
            public void onChanged(SetRatingResult ratingResult) {
                if (ratingResult == null) {
                    return;
                }
                if (ratingResult.getError() != null) {
                    Toast.makeText(getApplicationContext(), R.string.set_rating_failed, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ratingResult.getSuccess() != null) {
                    Toast.makeText(getApplicationContext(), R.string.commit_rating, Toast.LENGTH_SHORT).show();
                    submitBttn.setVisibility(View.GONE);
                    ratingBar.setRating((float) ratingResult.getSuccess().getRating());
                    ratingBar.setFocusable(false);
                    ratingBar.setIsIndicator(true);

                    getRatingViewModel.getRating(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                }
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
                intentPop.putExtra("UserLogged", user);
                intentPop.putExtra("Activity", activity);
                startActivity(intentPop);
            }
        });

        /*Image actImage = activity.getImage();
        if (actImage != null) {
            activityImage.setVisibility(View.VISIBLE);
            byte[] img = actImage.getImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            activityImage.setImageBitmap(bitmap);
        }*/

        registerCommentViewModel.getRegisterCommentResult().observe(this, new Observer<RegisterCommentResult>() {
            @Override
            public void onChanged(RegisterCommentResult registerCommentResult) {
                if (registerCommentResult == null) {
                    return;
                }
                if (registerCommentResult.getError() != null) {
                    showActionFailed(registerCommentResult.getError());
                    return;
                }
                if (registerCommentResult.getSuccess() != null) {
                    Toast.makeText(getApplicationContext(), R.string.register_comment, Toast.LENGTH_SHORT).show();
                    editTextComment.setText("");
                    getActCommentsViewModel.getActComments(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                }
            }
        });

        postCommentBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //register the comment
                String comment = editTextComment.getText().toString();
                registerCommentViewModel.registerComment(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId(), comment);
                getActCommentsViewModel.getActComments(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
            }
        });

        getActCommentsViewModel.getActComments(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());

        getActCommentsViewModel.getActCommentsList().observe(this, comments -> {
            commentaryList = comments;
            CommentsRecyclerViewAdapter adapter = new CommentsRecyclerViewAdapter(getApplicationContext(), mActivity, commentaryList, user, activity);
            actCommentsRecyclerView.setAdapter(adapter);
            actCommentsRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        });

        textViewConfirmPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityInfoActivity.this, ConfirmParticipantsActivity.class);
                intent.putExtra("UserLogged", user);
                intent.putExtra("Activity", activity);
                startActivity(intent);
            }
        });

       /* checkPartBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityInfoActivity.this, ConfirmParticipantsActivity.class);
                intent.putExtra("UserLogged", user);
                intent.putExtra("Activity", activity);
                startActivity(intent);
            }
        });*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @androidx.annotation.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == AutocompleteActivity.RESULT_OK) {
            Place pl = Autocomplete.getPlaceFromIntent(data);
            editTextAddress.setText(pl.getAddress());
        } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
            Status status = Autocomplete.getStatusFromIntent(data);
        }
    }

 /*   private void showImageType() {
        switch (activity.getType()) {
            case "animals":
                imageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_animals, getApplicationContext().getTheme()));
                break;
            case "elderly":
                imageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_elderly, getApplicationContext().getTheme()));
                break;
            case "children":
                imageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_children, getApplicationContext().getTheme()));
                break;
            case "houseBuilding":
                imageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_disabled, getApplicationContext().getTheme()));
                break;
            case "health":
                imageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_health, getApplicationContext().getTheme()));
                break;
            case "nature":
                imageType.setImageDrawable(getResources().getDrawable(R.drawable.ic_nature, getApplicationContext().getTheme()));
                break;
        }
    }*/

    private int monthToIntegerShort(String month) {
        int result = 0;
        switch (month) {
            case "Jan":
                result = 0;
                break;
            case "Fev":
                result = 1;
                break;
            case "Mar":
                result = 2;
                break;
            case "Apr":
                result = 3;
                break;
            case "May":
                result = 4;
                break;
            case "Jun":
                result = 5;
                break;
            case "Jul":
                result = 6;
                break;
            case "Aug":
                result = 7;
                break;
            case "Sep":
                result = 8;
                break;
            case "Oct":
                result = 9;
                break;
            case "Nov":
                result = 10;
                break;
            case "Dec":
                result = 11;
                break;
        }
        return result;
    }

    private void showActionFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void hideOwnerFunctionalities() {
        //if the user is not the owner of the activity

        editTextActName.setFocusable(false);
        editTextActName.setClickable(false);
        editTextActName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editTextActOwner.setFocusable(false);
        editTextActOwner.setClickable(false);
        editTextActOwner.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editTextAddress.setFocusable(false);
        editTextAddress.setClickable(false);
        editTextAddress.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editTextNumPart.setFocusable(false);
        editTextNumPart.setClickable(false);
        editTextNumPart.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editTextTime.setFocusable(false);
        editTextTime.setClickable(false);
        editTextTime.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        editTextDuration.setFocusable(false);
        editTextDuration.setClickable(false);
        editTextDuration.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        saveUpdateBttn.setVisibility(View.GONE);
        deleteActBttn.setVisibility(View.GONE);

    }

}
