package pt.vow.ui.getActivities;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import pt.vow.R;
import pt.vow.databinding.ActivityMainPageBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;

public class MainPage extends AppCompatActivity {

    private ActivityMainPageBinding binding;
    private LoggedInUserView user;
    private GetActivitiesViewModel activitiesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*activitiesViewModel = new ViewModelProvider(this, new GetActivitiesViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetActivitiesViewModel.class);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");

        activitiesViewModel.getActivities(user.getUsername(), String.valueOf(user.getTokenID()));

        activitiesViewModel.getActivitiesResult().observe(this, new Observer<GetActivitiesResult>() {
            @Override
            public void onChanged(@Nullable GetActivitiesResult getActivitiesResult) {
                if (getActivitiesResult == null) {
                    return;
                }
                if (getActivitiesResult.getError() != null) {
                    showGetActivitiesFailed(getActivitiesResult.getError());
                }
                if (getActivitiesResult.getSuccess() != null) {
                    setResult(android.app.Activity.RESULT_OK);
                    //finish();
                }
                //Complete and destroy login activity once successful
                //finish();
            }
        });
*/
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_feed,
                R.id.navigation_map, R.id.navigation_profile, R.id.navigation_new_activity)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_page);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }


    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

}