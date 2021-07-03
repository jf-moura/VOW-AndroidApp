package pt.vow.ui.frontPage;

import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import pt.vow.R;
import pt.vow.ui.login.LoginActivity;
import pt.vow.ui.profile.ProfileFragment;
import pt.vow.ui.register.RegisterActivity;

public class FrontPageActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "12345";
    private FrontPageActivity loginAct;
    private FrontPageActivity registerChooseAct;
    private Button loginBttn, createAccBttn, notificationBttn;
    private int notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frontpage);
        loginAct = this;
        registerChooseAct = this;
        loginBttn = findViewById(R.id.loginBttn);
        createAccBttn = findViewById(R.id.createAccBttn);
        notificationBttn = findViewById(R.id.notificationBttn);
        final TextView textViewInfoSite = findViewById(R.id.textViewInfoSite);

        notificationId = 0;
        createNotificationChannel();

        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginAct, LoginActivity.class);
                startActivity(intent);
            }
        });

        createAccBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registerChooseAct, RegisterActivity.class);
                startActivity(intent);
            }
        });

        notificationBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerNotification();
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            final boolean fromNotification = extras.getBoolean("NOTIFICATION");
            if (fromNotification) {
                ProfileFragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.front_page, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

    private void triggerNotification() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String message = getString(R.string.message_notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_compass))
                .setContentTitle("Rate your Activity")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
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
}