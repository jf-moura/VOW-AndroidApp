package pt.vow.ui.enroll;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityParticipantsResult;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModel;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.maps.MapsFragment;
import pt.vow.ui.profile.GetActivitiesByUserResult;
import pt.vow.ui.profile.GetActivitiesByUserViewModel;
import pt.vow.ui.profile.GetActivitiesByUserViewModelFactory;

public class EnrollActivity extends AppCompatActivity {

    private TextView textViewDuration, textViewNumPart, textViewTime, textViewActName, textViewActOwner, textViewAddress;
    private Button enrollButton, directionsButton;
    private EnrollViewModel enrollViewModel;
    private CancelEnrollViewModel cancelEnrollViewModel;
    private LoggedInUserView user;
    private String[] activityInfo;
    private String activityInfoTitle;
    private List<Activity> activitiesList;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private ActivityParticipantsViewModel actParticipantsViewModel;
    private Activity aux;
    private EnrollActivity mActivity;
    private ImageView imageType;

    private Observer<GetActivitiesByUserResult> actByUserObs;

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
        directionsButton = findViewById(R.id.btnDirections);

        imageType = findViewById(R.id.imageViewType);

        enrollViewModel = new ViewModelProvider(this, new EnrollViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(EnrollViewModel.class);

        cancelEnrollViewModel = new ViewModelProvider(this, new CancelEnrollViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(CancelEnrollViewModel.class);

        getActivitiesByUserViewModel = new ViewModelProvider(this, new GetActivitiesByUserViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetActivitiesByUserViewModel.class);

        actParticipantsViewModel = new ViewModelProvider(this, new ActivityParticipantsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ActivityParticipantsViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activityInfoTitle = (String) getIntent().getSerializableExtra("ActivityInfo");

        activityInfo = activityInfoTitle.split("_");


        textViewActName.setText(Html.fromHtml("<b>" + getResources().getString(R.string.activity_name) + "</b>" + " " + activityInfo[0]));
        textViewActOwner.setText(Html.fromHtml("<b>" + getResources().getString(R.string.organization) + "</b>" + " " + activityInfo[1]));
        textViewAddress.setText(Html.fromHtml("<b>" + getResources().getString(R.string.address) + "</b>" + " " + activityInfo[2]));
        textViewTime.setText(Html.fromHtml("<b>" + getResources().getString(R.string.time) + "</b>" + " " + activityInfo[3]));
        textViewNumPart.setText(Html.fromHtml("<b>" + getResources().getString(R.string.number_participants) + "</b>" + " " + activityInfo[4]));
        textViewDuration.setText(Html.fromHtml("<b>" + getResources().getString(R.string.duration) + "</b>" + " " + Integer.parseInt(activityInfo[5]) / 60 + "h" + Integer.parseInt(activityInfo[5]) % 60));

        getActivitiesByUserViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));
        getActivitiesByUserViewModel.getActivitiesResult().observeForever(actByUserObs = new Observer<GetActivitiesByUserResult>() {
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
                                showImageType();
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
                    if (participants.size() == Integer.parseInt(activityInfo[4])) {
                        enrollButton.setEnabled(false);
                    } else enrollButton.setEnabled(true);
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

        directionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("getDirections", true);
                bundle.putString("destination", activityInfo[2]);
                Fragment fragment = new MapsFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.enroll_activity, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (actByUserObs != null)
            getActivitiesByUserViewModel.getActivitiesResult().removeObserver(actByUserObs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "URL da app");
            startActivity(intent.createChooser(intent, "Share Using"));
        }
        return super.onOptionsItemSelected(item);
    }

    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(this.getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showImageType() {
        switch (aux.getType()) {
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

}
