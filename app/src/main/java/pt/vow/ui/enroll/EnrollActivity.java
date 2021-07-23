package pt.vow.ui.enroll;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModel;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.image.Image;
import pt.vow.ui.maps.GetRouteCoordResult;
import pt.vow.ui.maps.GetRouteCoordViewModelFactory;
import pt.vow.ui.maps.GetRouteCoordinatesViewModel;
import pt.vow.ui.maps.MapsFragment;
import pt.vow.ui.profile.ActivitiesByUserView;

public class EnrollActivity extends AppCompatActivity {
    private TextView textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private Button enrollButton, directionsButton;
    private ImageView activityImage;

    private EnrollViewModel enrollViewModel;
    private CancelEnrollViewModel cancelEnrollViewModel;
    private GetRouteCoordinatesViewModel getRouteCoordinatesViewModel;
    private LoggedInUserView user;
    private Activity activity;
    private ActivitiesByUserView activitiesList;
    private ActivityParticipantsViewModel actParticipantsViewModel;
    private Activity aux;
    private EnrollActivity mActivity;
    private ImageView imageType;
    private String dest;

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
        activityImage = findViewById(R.id.activityImageEnroll);

        enrollButton = findViewById(R.id.enrollButton);
        directionsButton = findViewById(R.id.btnDirections);

        imageType = findViewById(R.id.imageViewType);

        dest = "";

        enrollViewModel = new ViewModelProvider(this, new EnrollViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(EnrollViewModel.class);
        cancelEnrollViewModel = new ViewModelProvider(this, new CancelEnrollViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(CancelEnrollViewModel.class);
        actParticipantsViewModel = new ViewModelProvider(this, new ActivityParticipantsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ActivityParticipantsViewModel.class);

        getRouteCoordinatesViewModel = new ViewModelProvider(this, new GetRouteCoordViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetRouteCoordinatesViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");
        activitiesList = (ActivitiesByUserView) getIntent().getSerializableExtra("EnrolledActivities");

        getSupportActionBar().setTitle(activity.getName());

        textViewActName.setText(Html.fromHtml("<b>" + getResources().getString(R.string.activity_name) + "</b>" + " " + activity.getName()));
        textViewActOwner.setText(Html.fromHtml("<b>" + getResources().getString(R.string.organization) + "</b>" + " " + activity.getOwner()));
        textViewAddress.setText(Html.fromHtml("<b>" + getResources().getString(R.string.address) + "</b>" + " " + activity.getAddress()));
        textViewTime.setText(Html.fromHtml("<b>" + getResources().getString(R.string.time) + "</b>" + " " + activity.getTime()));
        textViewNumPart.setText(Html.fromHtml("<b>" + getResources().getString(R.string.number_participants) + "</b>" + " " + activity.getParticipantNum()));
        textViewDuration.setText(Html.fromHtml("<b>" + getResources().getString(R.string.duration) + "</b>" + " " + Integer.parseInt(activity.getDurationInMinutes()) / 60 + "h" + Integer.parseInt(activity.getDurationInMinutes()) % 60));

        if (activitiesList != null) {
            for (Activity a : activitiesList.getActivities()) {
                if (a.getId().equals(activity.getId())) {
                    aux = a;
                    break;
                }
            }
        }
        if (aux != null) { //it means that user already joined the activity
            enrollButton.setText(getResources().getString(R.string.unjoin));
        }

        showImageType();

        actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());

        actParticipantsViewModel.getParticipantsList().observe(this, participants -> {
            textViewNumPart.setText(Html.fromHtml("<b>" + getResources().getString(R.string.number_participants) + " </b>" + participants.size() + "/" + activity.getParticipantNum()));
            if (participants.size() == Integer.parseInt(activity.getParticipantNum())) {
                enrollButton.setEnabled(false);
            }
            //else enrollButton.setEnabled(true);
        });

        Image actImage = activity.getImage();
        if (actImage != null) {
            activityImage.setVisibility(View.VISIBLE);
            byte[] img = actImage.getImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            activityImage.setImageBitmap(bitmap);
        }

        enrollViewModel.getEnrollResult().observe(this, new Observer<EnrollResult>() {
            @Override
            public void onChanged(EnrollResult enrollResult) {
                if (enrollResult == null) {
                    return;
                }
                if (enrollResult.getError() != null) {
                    showActionFailed(enrollResult.getError());
                    return;
                }
                if (enrollResult.getSuccess() != null) {
                    Toast.makeText(getApplicationContext(), R.string.joined_activity, Toast.LENGTH_SHORT).show();
                    enrollButton.setText(getResources().getString(R.string.unjoin));
                    actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                }
            }
        });

        cancelEnrollViewModel.getCancelEnrollResult().observe(this, new Observer<CancelEnrollResult>() {
            @Override
            public void onChanged(CancelEnrollResult cancelEnrollResult) {
                if (cancelEnrollResult == null) {
                    return;
                }
                if (cancelEnrollResult.getError() != null) {
                    showActionFailed(cancelEnrollResult.getError());
                    return;
                }
                if (cancelEnrollResult.getSuccess() != null) {
                    enrollButton.setText(getResources().getString(R.string.join));
                    Uri deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, Long.parseLong(activity.getId()));
                    //getContentResolver().delete(deleteUri, null, null);
                    //Toast.makeText(getApplicationContext(), R.string.unjoined_activity, Toast.LENGTH_SHORT).show();
                    actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                }
            }
        });

        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (enrollButton.getText().equals(getResources().getString(R.string.join))) {
                    enrollViewModel.enrollInActivity(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                    // TODO: Falta ver os erros
                    Intent intentPop = new Intent(EnrollActivity.this, Pop.class);
                    intentPop.putExtra("id", activity.getId());
                    intentPop.putExtra("title", activity.getName());
                    intentPop.putExtra("location", activity.getAddress());
                    intentPop.putExtra("time", activity.getTime());
                    intentPop.putExtra("duration", Integer.parseInt(activity.getDurationInMinutes()) / 60 + ":" + Integer.parseInt(activity.getDurationInMinutes()) % 60);
                    startActivity(intentPop);
                }
                //if a user is already in join and user wants to cancel
                else {
                    cancelEnrollViewModel.cancelEnrollInActivity(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                }
            }
        });


        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //quer dizer que e uma rota por isso temos de ir buscar o getRouteCoordinates
                if (activity.getCoordinates().isEmpty()) {
                    getCoordinates();
                } else { //atividade unica
                    dest = activity.getAddress();
                    doDirections();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(this.getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showImageType() {
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
    }

    private void showActionFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }

    private void doDirections() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("getDirections", true);
        bundle.putString("destination", dest);
        Fragment fragment = new MapsFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.enroll_activity, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void getCoordinates() {
        getRouteCoordinatesViewModel.getCoordinates(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId(), activity);
        getRouteCoordinatesViewModel.getRouteCoordResult().observe(mActivity, new Observer<GetRouteCoordResult>() {
            @Override
            public void onChanged(GetRouteCoordResult getRouteCoordResult) {
                if (getRouteCoordResult == null) {
                    return;
                }
                if (getRouteCoordResult.getError() != null) {
                    showActionFailed(getRouteCoordResult.getError());
                }
                if (getRouteCoordResult.getSuccess() != null) {
                    if (getRouteCoordResult.getSuccess().getCoordinates().size() < 10) {
                        for (String coord : getRouteCoordResult.getSuccess().getCoordinates()) {
                            dest += coord + "/";
                        }
                        doDirections();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.route_too_big, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
