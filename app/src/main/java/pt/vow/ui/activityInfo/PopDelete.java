package pt.vow.ui.activityInfo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.disableActivity.DeleteActivityResult;
import pt.vow.ui.disableActivity.DeleteActivityViewModel;
import pt.vow.ui.disableActivity.DeleteActivityViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.ProfileFragment;

public class PopDelete extends AppCompatActivity {
    private static final String CHANNEL_ID = "12345";

    private Button buttonYes, buttonNo;
    private TextView textView;
    private DeleteActivityViewModel deleteActivityViewModel;
    private LoggedInUserView user;
    private Activity activity;
    private PopDelete mActivity;
    private int notificationId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        mActivity = this;

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");

        deleteActivityViewModel = new ViewModelProvider(this, new DeleteActivityViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(DeleteActivityViewModel.class);

        notificationId = 0;
        createNotificationChannel();

        buttonYes = findViewById(R.id.buttonYes);
        buttonNo = findViewById(R.id.buttonNo);
        textView = findViewById(R.id.textViewPopUp);
        textView.setText(R.string.confirm_delete);
        textView.setGravity(Gravity.CENTER);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .7), (int) (height * .25));

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete the activity
                deleteActivityViewModel.deleteActivity(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
                deleteActivityViewModel.getDeleteActivityResult().observe(mActivity, new Observer<DeleteActivityResult>() {
                    @Override
                    public void onChanged(DeleteActivityResult deleteActivityResult) {
                        if (deleteActivityResult == null) {
                            return;
                        }
                        if (deleteActivityResult.getError() != null) {
                            showActionFailed(deleteActivityResult.getError());
                            return;
                        }
                        if (deleteActivityResult.getSuccess() != null) {
                            Toast.makeText(getApplicationContext(), R.string.activity_deleted, Toast.LENGTH_SHORT).show();

                            //go back to profile fragment
                           /* FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                            Fragment fragment = new ProfileFragment();
                            fragmentTransaction.replace(R.id.popWindow, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();*/
                            triggerNotification(activity.getName());
                            finish();
                        }
                    }
                });
            }
        });

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void triggerNotification(String name) {
        // Create an explicit intent for an Activity in your app
       /* Intent intent = new Intent(this, ActivityInfoActivity.class);
        intent.putExtra("NOTIFICATION", true);
        intent.putExtra("UserLogged", user);
        intent.putExtra("Activity", a);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
*/
        String message = getString(R.string.not_deleted_act);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_fi_rr_trash)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_foreground))
               // .setContentTitle(getResources().getString(R.string.rate_activity))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(message + " " + name)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message + ": " + name))
              //  .setContentIntent(pendingIntent)
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


    private void showActionFailed(@StringRes Integer error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
    }
}