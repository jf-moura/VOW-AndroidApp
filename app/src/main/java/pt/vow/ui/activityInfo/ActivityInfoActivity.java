package pt.vow.ui.activityInfo;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.data.model.Commentary;
import pt.vow.data.model.UserInfo;
import pt.vow.ui.VOW;
import pt.vow.ui.comments.CommentsRecyclerViewAdapter;
import pt.vow.ui.comments.GetActCommentsResult;
import pt.vow.ui.comments.GetActCommentsViewModel;
import pt.vow.ui.comments.GetActCommentsViewModelFactory;
import pt.vow.ui.comments.RegisterCommentResult;
import pt.vow.ui.comments.RegisterCommentViewModel;
import pt.vow.ui.comments.RegisterCommentViewModelFactory;
import pt.vow.ui.confimParticipants.ConfirmPartRecyclerViewAdapter;
import pt.vow.ui.confimParticipants.ConfirmParticipantsActivity;
import pt.vow.ui.confimParticipants.ConfirmParticipantsViewModel;
import pt.vow.ui.confimParticipants.ConfirmParticipantsViewModelFactory;
import pt.vow.ui.confimParticipants.ParticipantsConfirmedViewModel;
import pt.vow.ui.confimParticipants.ParticipantsConfirmedViewModelFactory;
import pt.vow.ui.disableActivity.DeleteActivityViewModel;
import pt.vow.ui.disableActivity.DeleteActivityViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.image.Image;
import pt.vow.ui.image.UploadImageViewModel;
import pt.vow.ui.image.UploadImageViewModelFactory;
import pt.vow.ui.update.UpdateActivityViewModel;
import pt.vow.ui.update.UpdateActivityViewModelFactory;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TimeZone;

public class ActivityInfoActivity extends AppCompatActivity {

    private ActivityInfoActivity mActivity;
    private TextView textActName, textDescription, textOwner, textAddress, textTime, textPartNum, textDuration, textViewRating, textViewConfirmPart;
    private EditText editTextNumPart, editTextActName, editTextAddress, editTextDescription;
    private ImageButton editNumPart, editTime, editActName, editDuration, editAddress, editDescription;
    private ImageButton cancelEditNumPart, cancelEditActName, cancelEditDuration, cancelEditAddress, cancelEditDescription;
    private TimePicker editTextDuration;
    private FloatingActionButton camera;
    private Button submitBttn, saveUpdateBttn, postCommentBttn, addImageBttn;
    private EditText editTextComment;
    private RatingBar ratingBar;
    private ImageView activityImage;

    private LoggedInUserView user;
    private Activity activity;
    private List<String> confirmedPart;
    private List<String> participantsList;
    private GetRatingViewModel getRatingViewModel;
    private SetRatingViewModel setRatingViewModel;
    private UploadImageViewModel uploadImageViewModel;
    private DeleteActivityViewModel deleteActivityViewModel;
    private ActivityParticipantsViewModel actParticipantsViewModel;
    private ParticipantsConfirmedViewModel participantsConfirmedViewModel;
    private double totalRate;
    private String durationInMinutes, date;
    private Uri imageUri;
    private static Bitmap bitmap;
    //private long rate;
    private Observer<GetRatingResult> rateObs;
    private ImageButton deleteActBttn;
    private ImageView imageType;
    private RegisterCommentViewModel registerCommentViewModel;
    private GetActCommentsViewModel getActCommentsViewModel;

    private List<Commentary> commentaryList;
    private RecyclerView actCommentsRecyclerView;
    private UpdateActivityViewModel updateActivityViewModel;

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
        updateActivityViewModel = new ViewModelProvider(this, new UpdateActivityViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(UpdateActivityViewModel.class);
        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");

        actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
        getActCommentsViewModel.getActComments(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());

        getSupportActionBar().setTitle(activity.getName());

        textActName = findViewById(R.id.textViewActName);
        textOwner = findViewById(R.id.textViewOwner);
        textAddress = findViewById(R.id.textViewAddress);
        textDuration = findViewById(R.id.textViewDur);
        textPartNum = findViewById(R.id.textViewPartNum);
        textTime = findViewById(R.id.textViewTime);
        textDescription = findViewById(R.id.textViewDescription);

        editTextActName = findViewById(R.id.editTextActName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextDuration = findViewById(R.id.editTextDur);
        editTextNumPart = findViewById(R.id.editTextPartNum);
        editTextDescription = findViewById(R.id.editTextDescription);

        editActName = findViewById(R.id.editNameBttn);
        editAddress = findViewById(R.id.editAddressBttn);
        editDuration = findViewById(R.id.editDurationBttn);
        editNumPart = findViewById(R.id.editPartNumBttn);
        editTime = findViewById(R.id.editTimeBttn);
        editDescription = findViewById(R.id.editDescriptionBttn);

        cancelEditActName = findViewById(R.id.cancelEditNameBttn);
        cancelEditAddress = findViewById(R.id.cancelEditAddressBttn);
        cancelEditDuration = findViewById(R.id.cancelEditDurBttn);
        cancelEditNumPart = findViewById(R.id.cancelEditPartNumBttn);
        cancelEditDescription = findViewById(R.id.cancelEditDescriptionBttn);

        ratingBar = findViewById(R.id.rating_bar);
        submitBttn = findViewById(R.id.submitBttn);
        saveUpdateBttn = findViewById(R.id.submitChangesBttn);
        editTextComment = findViewById(R.id.editTextComment);
        textViewRating = findViewById(R.id.textViewRating);

        deleteActBttn = findViewById(R.id.deleteActBttn);
        addImageBttn = findViewById(R.id.addImage);
        camera = findViewById(R.id.camera);
        activityImage = findViewById(R.id.activityImageInfo);
        postCommentBttn = findViewById(R.id.buttonPostComment);
        actCommentsRecyclerView = findViewById(R.id.comments_recycler_view);
        textViewConfirmPart = findViewById(R.id.textViewConfirmPart);

        // showImageType();


        //if the user is the owner of the activity
        if (user.getUsername().equals(activity.getOwner()))
            showOwnerFunctionalities();

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

        actParticipantsViewModel.getParticipantsList().observe(this, participants -> {
            participantsList = participants;
            textPartNum.setText(participants.size() + "/" + activity.getParticipantNum());
        });

        textActName.setText(" " + activity.getName());
        textOwner.setText(" " + activity.getOwner());
        textAddress.setText(" " + activity.getAddress());
        textTime.setText(" " + activity.getTime());
        textDuration.setText(" " + Integer.parseInt(activity.getDurationInMinutes()) / 60 + "h" + Integer.parseInt(activity.getDurationInMinutes()) % 60);
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

        Image actImage = activity.getImage();
        if (actImage != null) {
            if (user.getUsername().equals(activity.getOwner()))
                camera.setVisibility(View.VISIBLE);
            activityImage.setVisibility(View.VISIBLE);
            byte[] img = actImage.getImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            activityImage.setImageBitmap(bitmap);
        } else if (user.getUsername().equals(activity.getOwner()))
            addImageBttn.setVisibility(View.VISIBLE);

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

        class SortbyTimestamp implements Comparator<Commentary> {

            // Used for sorting in ascending order of
            // timestamp
            public int compare(Commentary a, Commentary b) {
                int ca = getTimeStamp(a);
                int cb = getTimeStamp(b);

                return cb - ca;
            }
        }


        getActCommentsViewModel.getActCommentsList().observe(this, comments -> {
            commentaryList = comments;
            if (!comments.isEmpty())
                actCommentsRecyclerView.setVisibility(View.VISIBLE);
            Collections.sort(commentaryList, new SortbyTimestamp());
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
                intent.putExtra("Participants", (Serializable) participantsList);
                intent.putExtra("ConfirmedParticipants", (Serializable) confirmedPart);
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

        editActName.setOnClickListener(v -> {
            textActName.setVisibility(View.INVISIBLE);
            editTextActName.setVisibility(View.VISIBLE);
            editActName.setVisibility(View.INVISIBLE);
            cancelEditActName.setVisibility(View.VISIBLE);
        });

        editDescription.setOnClickListener(v -> {
            textDescription.setVisibility(View.INVISIBLE);
            editTextDescription.setVisibility(View.VISIBLE);
            editDescription.setVisibility(View.INVISIBLE);
            cancelEditDescription.setVisibility(View.VISIBLE);
        });

        //TODO: ADDRESS UPDATE

        editDuration.setOnClickListener(v -> {
            textDuration.setVisibility(View.INVISIBLE);
            editTextDuration.setVisibility(View.VISIBLE);
            editDuration.setVisibility(View.INVISIBLE);
            cancelEditDuration.setVisibility(View.VISIBLE);
        });

        editNumPart.setOnClickListener(v -> {
            textPartNum.setVisibility(View.INVISIBLE);
            editTextNumPart.setVisibility(View.VISIBLE);
            editNumPart.setVisibility(View.INVISIBLE);
            cancelEditNumPart.setVisibility(View.VISIBLE);
        });

        cancelEditActName.setOnClickListener(v -> {
            textActName.setVisibility(View.VISIBLE);
            editTextActName.setVisibility(View.GONE);
            editActName.setVisibility(View.VISIBLE);
            cancelEditActName.setVisibility(View.GONE);
        });

        cancelEditDescription.setOnClickListener(v -> {
            textDescription.setVisibility(View.VISIBLE);
            editTextDescription.setVisibility(View.GONE);
            editDescription.setVisibility(View.VISIBLE);
            cancelEditDescription.setVisibility(View.GONE);
        });

        //TODO: ADDRESS UPDATE

        cancelEditDuration.setOnClickListener(v -> {
            textDuration.setVisibility(View.VISIBLE);
            editTextDuration.setVisibility(View.GONE);
            editDuration.setVisibility(View.VISIBLE);
            cancelEditDuration.setVisibility(View.GONE);
        });

        cancelEditNumPart.setOnClickListener(v -> {
            textPartNum.setVisibility(View.VISIBLE);
            editTextNumPart.setVisibility(View.GONE);
            editNumPart.setVisibility(View.VISIBLE);
            cancelEditNumPart.setVisibility(View.GONE);
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                updateActivityViewModel.updateActivityDataChanged(editTextActName.getText().toString(), editTextAddress.getText().toString(),
                        date, activity.getType(), editTextNumPart.getText().toString(),
                        durationInMinutes, editTextDescription.getText().toString());
                saveUpdateBttn.setEnabled(true);
            }
        };

        editTextActName.addTextChangedListener(afterTextChangedListener);
        editTextAddress.addTextChangedListener(afterTextChangedListener);
        editTextNumPart.addTextChangedListener(afterTextChangedListener);
        editTextDescription.addTextChangedListener(afterTextChangedListener);
        saveUpdateBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateActivityViewModel.updateActivity(editTextActName.getText().toString(), editTextAddress.getText().toString(), activity.getCoordinates(),
                        date, activity.getType(), editTextNumPart.getText().toString(),
                        durationInMinutes, "", "", String.valueOf(user.getRole()), editTextDescription.getText().toString());
                resetButtons();
            }
        });

        editTextDuration.setOnTimeChangedListener(
                (view, hourOfDay, minute) -> {
                    int hour = editTextDuration.getHour();
                    int minutes = editTextDuration.getMinute();
                    int aux = hour * 60 + minutes;
                    durationInMinutes = new String().concat(String.valueOf(aux));
                    updateActivityViewModel.updateActivityDataChanged(editTextActName.getText().toString(), editTextAddress.getText().toString(),
                            date, activity.getType(), editTextNumPart.getText().toString(),
                            durationInMinutes, editTextDescription.getText().toString());
                    saveUpdateBttn.setEnabled(true);
                });

        editTime.setOnClickListener(v -> showDatePickerDialog());

        addImageBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);

                someActivityResultLauncher.launch(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        participantsConfirmedViewModel = new ViewModelProvider(this, new ParticipantsConfirmedViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ParticipantsConfirmedViewModel.class);
        participantsConfirmedViewModel.getParticipants(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());

        participantsConfirmedViewModel.getParticipantsList().observe(this, participantsConfirmed -> {
            confirmedPart = participantsConfirmed;
        });

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                            activityImage.setImageBitmap(bitmap);
                            activityImage.setVisibility(View.VISIBLE);
                            camera.setVisibility(View.VISIBLE);
                            addImageBttn.setVisibility(View.GONE);
                            uploadImage("vow-project-311114", "vow_profile_pictures", activity.getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadImage(String projectId, String bucketName, String objectName) throws IOException {
        if (imageUri != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            getResizedBitmap(bitmap, 1000);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] imageInByte = out.toByteArray();
            out.close();
            uploadImageViewModel.uploadImage(projectId, bucketName, objectName, imageInByte);
        }
    }

    public void getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        bitmap = Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();

        DatePickerDialog dpd = new DatePickerDialog(mActivity, (view, year, monthOfYear, dayOfMonth) -> {
            cal.set(year, monthOfYear, dayOfMonth);
            new TimePickerDialog(mActivity, (view1, hourOfDay, minute) -> {
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);

                String timeZone = TimeZone.getTimeZone("GMT").getDisplayName(false, TimeZone.SHORT);
                // TODO: timezone does not yet change due to winter/summer time
                date = new String().concat(String.valueOf(dayOfMonth)).concat("/")
                        .concat(String.valueOf(monthOfYear + 1)).concat("/").concat(String.valueOf(year)).concat(" ").concat(String.valueOf(hourOfDay))
                        .concat(":").concat(String.valueOf(minute)).concat(" ").concat(timeZone);

                updateActivityViewModel.updateActivityDataChanged(editTextActName.getText().toString(), editTextAddress.getText().toString(),
                        date, activity.getType(), editTextNumPart.getText().toString(),
                        durationInMinutes, editTextDescription.getText().toString());
                saveUpdateBttn.setEnabled(true);
            }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show();
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        dpd.getDatePicker().setMinDate(cal.getTimeInMillis());
        dpd.show();
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

    private void showOwnerFunctionalities() {
        editActName.setVisibility(View.VISIBLE);
        editDescription.setVisibility(View.VISIBLE);
        editAddress.setVisibility(View.VISIBLE);
        editTime.setVisibility(View.VISIBLE);
        editNumPart.setVisibility(View.VISIBLE);
        editDuration.setVisibility(View.VISIBLE);
        saveUpdateBttn.setVisibility(View.VISIBLE);
        ratingBar.setVisibility(View.GONE);
        submitBttn.setVisibility(View.GONE);
        if (!activity.getStatus())
            deleteActBttn.setVisibility(View.INVISIBLE);
        else
            deleteActBttn.setVisibility(View.VISIBLE);
    }

    private void resetButtons() {
        editActName.setVisibility(View.VISIBLE);
        editDescription.setVisibility(View.VISIBLE);
        editAddress.setVisibility(View.VISIBLE);
        editTime.setVisibility(View.VISIBLE);
        editNumPart.setVisibility(View.VISIBLE);
        editDuration.setVisibility(View.VISIBLE);
        saveUpdateBttn.setEnabled(false);
        cancelEditActName.setVisibility(View.GONE);
        cancelEditDescription.setVisibility(View.GONE);
        cancelEditAddress.setVisibility(View.GONE);
        cancelEditNumPart.setVisibility(View.GONE);
        cancelEditDuration.setVisibility(View.GONE);
        //TODO: mudar activity info
    }

    private int getTimeStamp(Commentary a) {
        String[] dateTime = a.getCreationTime().split(" ");
        String[] hours = dateTime[3].split(":");

        Calendar beginTime = Calendar.getInstance();

        if (dateTime[4].equals("PM"))
            beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length() - 1)), Integer.valueOf(hours[0]) + 12, Integer.valueOf(hours[1]));
        else
            beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length() - 1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));

        return (int) beginTime.getTimeInMillis();
    }

}
