package pt.vow.ui.mainPage;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.io.IOException;

import pt.vow.R;
import pt.vow.databinding.ActivityMainPageOrganizationBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.feed.GetActivitiesViewModel;
import pt.vow.ui.feed.GetActivitiesViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.profile.GetActivitiesByUserViewModel;
import pt.vow.ui.profile.GetActivitiesByUserViewModelFactory;
import pt.vow.ui.profile.GetMyActivitiesViewModel;
import pt.vow.ui.profile.GetMyActivitiesViewModelFactory;

public class MainPageOrganization extends AppCompatActivity {

    private ActivityMainPageOrganizationBinding binding;
    private MainPageOrganization mActivity;
    private LoggedInUserView user;
    private GetActivitiesViewModel activitiesViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private byte[] profileImageInByte;

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

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");

        activitiesViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));
        getActivitiesByUserViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));
        getMyActivitiesViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));
        try {
            downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_profile, R.id.navigation_new_activity, R.id.navigation_podium, R.id.navigation_activities_data, R.id.navigation_feed)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

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
        });*/

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