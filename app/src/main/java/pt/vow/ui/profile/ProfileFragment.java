package pt.vow.ui.profile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import pt.vow.R;

import pt.vow.data.model.Activity;
import pt.vow.databinding.FragmentProfileBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityInfo;
import pt.vow.ui.extraInfo.UploadImageViewModel;
import pt.vow.ui.extraInfo.UploadImageViewModelFactory;
import pt.vow.ui.frontPage.FrontPageActivity;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.logout.LogoutViewModel;
import pt.vow.ui.logout.LogoutViewModelFactory;
import pt.vow.ui.update.UpdateActivity;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private static final int IMG_REQUEST = 21;
    private static final int RESULT_OK = -1;
    private static final String CHANNEL_ID = "012345";

    private ImageView menuImageView, menuImageViewClose;
    private ImageButton profileImage;
    private LinearLayout settingsLinearLayout, statsLinearLayout, logoutLinearLayout, linearLayoutPrincipal;
    private TextView myActivitiesTextView;

    private ProfileViewModel profileViewModel;
    private LogoutViewModel logoutViewModel;
    private RecyclerView recyclerView;
    private FragmentProfileBinding binding;
    private List<Activity> activitiesList;

    private Uri imageUri;
    private Bitmap bitmap;
    private UploadImageViewModel uploadImageViewModel;

    private LoggedInUserView user;
    private DrawerLayout drawerLayout;
    private int notificationId;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        getActivitiesByUserViewModel = new ViewModelProvider(this, new GetActivitiesByUserViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(GetActivitiesByUserViewModel.class);

        logoutViewModel = new ViewModelProvider(this, new LogoutViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(LogoutViewModel.class);

        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");


        recyclerView = root.findViewById(R.id.activities_recycler_view_profile);
        profileImage = root.findViewById(R.id.profileImage);
        myActivitiesTextView = root.findViewById(R.id.myActivitiesTextView);

        notificationId = 0;
        createNotificationChannel();

        loginPreferences = getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        getActivitiesByUserViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));
        getActivitiesByUserViewModel.getActivitiesResult().observeForever(new Observer<GetActivitiesByUserResult>() {
            @Override
            public void onChanged(@Nullable GetActivitiesByUserResult getActivitiesResult) {
                if (getActivitiesResult == null) {
                    return;
                }
                if (getActivitiesResult.getError() != null) {
                    showGetActivitiesFailed(getActivitiesResult.getError());
                }
                if (getActivitiesResult.getSuccess() != null) {
                    getActivitiesByUserViewModel.getActivitiesList().observe(getActivity(), list -> {
                        activitiesList = list;
                    });
                    if (activitiesList.size() == 0) {
                        myActivitiesTextView.setText(R.string.no_activities_available);
                    }
                    if (activitiesList != null) {
                        ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(getContext(), activitiesList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        /* for(Activity a : activitiesList){
                            Calendar currentTime = Calendar.getInstance();

                            String[] dateTime = a.getTime().split(" ");
                            String[] hours = dateTime[3].split(":");

                            Calendar beginTime = Calendar.getInstance();
                            beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));
                            long startMillis = beginTime.getTimeInMillis();
                            if(startMillis == currentTime.getTimeInMillis()){
                               triggerNotification(a, a.getName());
                            }
                        }*/
                    }

                    getActivity().setResult(android.app.Activity.RESULT_OK);
                }
            }
        });

        drawerLayout = root.findViewById(R.id.drawerLayout);
        menuImageView = root.findViewById(R.id.menuImageView);
        settingsLinearLayout = root.findViewById(R.id.settingsLinearLayout);
        statsLinearLayout = root.findViewById(R.id.statsLinearLayout);
        logoutLinearLayout = root.findViewById(R.id.logoutLinearLayout);
        menuImageViewClose = root.findViewById(R.id.menuImageViewClose);
        linearLayoutPrincipal = root.findViewById(R.id.linearLayoutPrincipal);


        if (user.getRole() == 0) { //volunteer
            statsLinearLayout.setVisibility(LinearLayout.GONE);
            ViewGroup.LayoutParams params = linearLayoutPrincipal.getLayoutParams();
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
            params.height = height;
            linearLayoutPrincipal.setLayoutParams(params);
        }

        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        menuImageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
            }
        });
        settingsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateActivity.class);
                intent.putExtra("UserLogged", user);
                startActivity(intent);
            }
        });
        statsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Statistics", Toast.LENGTH_SHORT).show();
            }
        });
        logoutLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutViewModel.logout(user.getUsername(),
                        user.getTokenID());

                loginPrefsEditor.putBoolean("saveLogin", false);
                Intent intent;
                //intent = new Intent(getActivity(), LoginActivity.class);
                intent = new Intent(getActivity(), FrontPageActivity.class);
                intent.putExtra("test", false);
                startActivity(intent);
                getActivity().finish();
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Image"), IMG_REQUEST);
            }
        });


        return root;
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                profileImage.setImageBitmap(bitmap);
                uploadImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void uploadImage(String projectId, String bucketName, String objectName) throws IOException {
        if (imageUri != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] imageInByte = out.toByteArray();
            out.close();
            uploadImageViewModel.uploadImage(projectId, bucketName, objectName, imageInByte);
        }
    }

    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void triggerNotification(Activity a, String name) {
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(getActivity(), ActivityInfo.class);
        intent.putExtra("NOTIFICATION", true);
        intent.putExtra("UserLogged", user);
        intent.putExtra("Activity", a);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, 0);

        String message = getString(R.string.message_notification);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.icon_foreground)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_compass))
                .setContentTitle("Rate your Activity")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText(message + " " + name)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
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
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null) {
            final boolean fromNotification = extras.getBoolean("NOTIFICATION");
            if (fromNotification) {
                ProfileFragment fragment = new ProfileFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.drawerLayout, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }
}