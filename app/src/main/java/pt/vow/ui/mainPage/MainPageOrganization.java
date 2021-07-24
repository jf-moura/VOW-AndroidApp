package pt.vow.ui.mainPage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;

import pt.vow.R;
import pt.vow.databinding.ActivityMainPageOrganizationBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.feed.GetActivitiesViewModel;
import pt.vow.ui.feed.GetActivitiesViewModelFactory;
import pt.vow.ui.getAllUsers.GetAllUsersViewModel;
import pt.vow.ui.getAllUsers.GetAllUsersViewModelFactory;
import pt.vow.ui.image.DownloadImageViewModel;
import pt.vow.ui.image.DownloadImageViewModelFactory;
import pt.vow.ui.image.Image;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.GetActivitiesByUserViewModel;
import pt.vow.ui.profile.GetActivitiesByUserViewModelFactory;
import pt.vow.ui.profile.GetMyActivitiesViewModel;
import pt.vow.ui.profile.GetMyActivitiesViewModelFactory;
import pt.vow.ui.profile.GetProfileViewModel;
import pt.vow.ui.profile.GetProfileViewModelFactory;
import pt.vow.ui.profile.ProfileInfoView;

public class MainPageOrganization extends AppCompatActivity {

    private ActivityMainPageOrganizationBinding binding;
    private MainPageOrganization mActivity;
    private LoggedInUserView user;
    private GetActivitiesViewModel activitiesViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private GetAllUsersViewModel getAllUsersViewModel;
    private GetProfileViewModel getProfileViewModel;

    private ProfileInfoView profileInfo;
    private Image profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);

        binding = ActivityMainPageOrganizationBinding.inflate(getLayoutInflater());
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
        getAllUsersViewModel = new ViewModelProvider(this, new GetAllUsersViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetAllUsersViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");

        getAllUsersViewModel.getAllUsers(user.getUsername(), user.getTokenID());
        getActivitiesByUserViewModel.getActivities(user.getUsername(), user.getUsername(), user.getTokenID());
        getProfileViewModel.getProfile(user.getUsername(), user.getUsername(), user.getTokenID());
        try {
            downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }
        getProfileViewModel.profile().observe(this, profile -> {
            profileInfo = profile;
        });

        profileImage = null;
        profileInfo = null;

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_choose_type, R.id.navigation_podium, R.id.navigation_activities_data, R.id.navigation_feed)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if (destination.getId() == R.id.navigation_profile) {
                if (profileInfo != null || profileImage != null) {
                    Bundle bundle = new Bundle();
                    if (profileInfo != null) {
                        destination.setLabel(profileInfo.getName());
                        bundle.putSerializable("ProfileInfo", profileInfo);
                    }
                    if (profileImage != null) {
                        bundle.putSerializable("ProfileImage", profileImage);
                    }
                    NavArgument arg = new NavArgument.Builder().setDefaultValue(bundle).build();
                    destination.addArgument("Profile", arg);
                }
            }
        });

        downloadImageViewModel.getImage().observe(this, image -> {
            if (image.getObjName().equals(user.getUsername())) {
                profileImage = image;
                byte[] profileImageInByte = image.getImageBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(profileImageInByte, 0, profileImageInByte.length);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                drawable.setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                Menu menu = navView.getMenu();
                menu.findItem(R.id.navigation_profile).setIcon(drawable);
            }
        });

        /*NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);
        NavController navController2 = navHostFragment.getNavController();
        NavGraph navGraph = navController2.getNavInflater().inflate(R.navigation.mobile_navigation_organization);
        navGraph.findNode();
        navController2.setGraph(navGraph);
        NavigationUI.setupWithNavController(binding.bottomNavView, navController2);*/

    }

    private void showImageDownloadFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}