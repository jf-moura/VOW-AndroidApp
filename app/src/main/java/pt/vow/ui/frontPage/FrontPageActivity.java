package pt.vow.ui.frontPage;

import android.app.Fragment;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
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

import pt.vow.MainPage;
import pt.vow.R;
import pt.vow.ui.getActivities.MainPageVolunteer;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.login.LoginActivity;
import pt.vow.ui.profile.ProfileFragment;
import pt.vow.ui.register.RegisterActivity;

public class FrontPageActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "12345";
    private FrontPageActivity loginAct;
    private FrontPageActivity registerChooseAct;
    private Button loginBttn, createAccBttn, notificationBttn;
    private int notificationId;

    private SharedPreferences loginPreferences;

    private boolean saveLogin, saveLogin2, saveLoginExtra;
    private String username, tokenId;
    private Long role;

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

        loginPreferences = getApplicationContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);

        Boolean test = (Boolean) getIntent().getSerializableExtra("test");
        if(test == null){
            test = true;
        }

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        saveLogin2 = loginPreferences.getBoolean("saveLogin2", true);

        if(username==null) {
            username = loginPreferences.getString("username", "username");
            role = loginPreferences.getLong("role", 1);
            tokenId = loginPreferences.getString("tokenId", "tokenId");
        }

       /* if (saveLogin && test) {
            Intent intent;
            LoggedInUserView user = new LoggedInUserView(role, username, tokenId);
            if (role == 0) { //volunteer
                intent = new Intent(loginAct, MainPageVolunteer.class);
                intent.putExtra("UserLogged", user);
            } else { //organization
                intent = new Intent(loginAct, MainPage.class);
                intent.putExtra("UserLogged", user);
            }
            startActivity(intent);
        }*/

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

    private void triggerNotification() {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String message = getString(R.string.message_notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_compass))
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("username", username);
        savedInstanceState.putLong("role", role);
        savedInstanceState.putString("tokenId", tokenId);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.
        username = savedInstanceState.getString("username");
        role = savedInstanceState.getLong("role");
        tokenId = savedInstanceState.getString("tokenId");
    }
}