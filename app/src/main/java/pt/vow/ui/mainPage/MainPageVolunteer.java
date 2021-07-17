package pt.vow.ui.mainPage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.databinding.ActivityMainPagePersonBinding;
import pt.vow.ui.activityInfo.ActivityInfoActivity;
import pt.vow.ui.feed.GetActivitiesViewModel;
import pt.vow.ui.feed.GetActivitiesViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.GetActivitiesByUserResult;
import pt.vow.ui.profile.GetActivitiesByUserViewModel;
import pt.vow.ui.profile.GetActivitiesByUserViewModelFactory;
import pt.vow.ui.profile.GetMyActivitiesViewModel;
import pt.vow.ui.profile.GetMyActivitiesViewModelFactory;
import pt.vow.ui.profile.GetProfileViewModel;
import pt.vow.ui.profile.GetProfileViewModelFactory;
import pt.vow.ui.profile.ProfileFragment;

public class MainPageVolunteer extends AppCompatActivity {
    private static final String CHANNEL_ID = "012345";

    private ActivityMainPagePersonBinding binding;
    private MainPageVolunteer mActivity;
    private LoggedInUserView user;
    private int notificationId;
    private GetActivitiesViewModel activitiesViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private GetProfileViewModel getProfileViewModel;

    private String profileName;
    private List<Activity> activitiesList;
    private byte[] profileImageInByte;

    private Observer<GetActivitiesByUserResult> actByUserObs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        binding = ActivityMainPagePersonBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mActivity = this;

        activitiesViewModel = new ViewModelProvider(this, new GetActivitiesViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetActivitiesViewModel.class);
        downloadImageViewModel = new ViewModelProvider(this, new DownloadImageViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(DownloadImageViewModel.class);
        getActivitiesByUserViewModel = new ViewModelProvider(this, new GetActivitiesByUserViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetActivitiesByUserViewModel.class);
        getMyActivitiesViewModel = new ViewModelProvider(this, new GetMyActivitiesViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetMyActivitiesViewModel.class);
        getProfileViewModel = new ViewModelProvider(this, new GetProfileViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetProfileViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        profileName = null;

        activitiesViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));
        getMyActivitiesViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));
        getActivitiesByUserViewModel.getActivities(user.getUsername(), user.getTokenID());
        getProfileViewModel.getProfile(user.getUsername(), user.getTokenID());
        try {
            downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }

        getProfileViewModel.profile().observe(this, profile -> {
            profileName = profile.getName();
        });

        notificationId = 0;
        createNotificationChannel();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_feed,
                R.id.navigation_new_activity, R.id.navigation_podium, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_page_person);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_profile && profileName != null) {
                destination.setLabel(profileName);
            }
        });

        /*downloadImageViewModel.getDownloadResult().observe(this, new Observer<GetImageResult>() {
            @Override
            public void onChanged(@Nullable GetImageResult downloadResult) {
                if (downloadResult == null) {
                    return;
                }
                if (downloadResult.getError() != null) {
                    showImageDownloadFailed(downloadResult.getError());
                }
                if (downloadResult.getSuccess() != null) {
                    profileImageInByte = downloadResult.getSuccess().getImage();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(profileImageInByte, 0, profileImageInByte.length);
                    Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                    Menu menu = navView.getMenu();
                    menu.findItem(R.id.navigation_profile).setIcon(drawable);
                }
            }
        });

        getActivitiesByUserViewModel.getActivitiesResult().observeForever(actByUserObs = new Observer<GetActivitiesByUserResult>() {
            @Override
            public void onChanged(@Nullable GetActivitiesByUserResult getActivitiesResult) {
                if (getActivitiesResult == null) {
                    return;
                }
                if (getActivitiesResult.getError() != null) {
                    showGetActivitiesFailed(getActivitiesResult.getError());
                }
                if (getActivitiesResult.getSuccess() != null && notificationId == 0) {
                    activitiesList = getActivitiesResult.getSuccess().getActivities();
                    if (activitiesList != null) {
                        for(Activity a : activitiesList){
                            Calendar currentTime = Calendar.getInstance();

                            String[] dateTime = a.getTime().split(" ");
                            String[] hours = dateTime[3].split(":");

                            Calendar beginTime = Calendar.getInstance();;
                            if (dateTime[4].equals("PM"))
                                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]) + 12 + Integer.valueOf(a.getDurationInMinutes())/60, Integer.valueOf(hours[1]) + Integer.valueOf(a.getDurationInMinutes())%60);
                            else
                                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]) +  Integer.valueOf(a.getDurationInMinutes())/60, Integer.valueOf(hours[1]) +  Integer.valueOf(a.getDurationInMinutes())%60);

                            long startMillis = beginTime.getTimeInMillis();
                            if(startMillis <= currentTime.getTimeInMillis()){
                                triggerNotification(a, a.getName());
                            }
                        }
                    }

                    setResult(android.app.Activity.RESULT_OK);
                }
            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
        if (actByUserObs != null)
            getActivitiesByUserViewModel.getActivitiesResult().removeObserver(actByUserObs);
    }

    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showImageDownloadFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void triggerNotification(Activity a, String name) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, ActivityInfoActivity.class);
        intent.putExtra("NOTIFICATION", true);
        intent.putExtra("UserLogged", user);
        intent.putExtra("Activity", a);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String message = getString(R.string.message_notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_foreground))
                .setContentTitle(getResources().getString( R.string.rate_activity))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(message + " " + name)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());
        notificationId++;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final boolean fromNotification = extras.getBoolean("NOTIFICATION");
            if (fromNotification) {
                ProfileFragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.drawerLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

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


}