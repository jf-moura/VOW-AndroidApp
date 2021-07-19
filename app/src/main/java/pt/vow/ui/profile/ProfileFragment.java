package pt.vow.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pt.vow.R;

import pt.vow.databinding.FragmentProfileBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.frontPage.FrontPageActivity;
import pt.vow.ui.mainPage.DownloadImageViewModel;
import pt.vow.ui.mainPage.GetImageResult;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.logout.LogoutViewModel;
import pt.vow.ui.logout.LogoutViewModelFactory;
import pt.vow.ui.update.UpdateActivity;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private static final int RESULT_OK = -1;

    private ShapeableImageView profileImage;
    private TextView aboutMeTextView;
    private LinearLayout settingsLinearLayout, statsLinearLayout, logoutLinearLayout, deleteAccountLinearLayout;
    private DrawerLayout drawerLayout;
    private BottomNavigationView topNavigationProfile;

    private LogoutViewModel logoutViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private UploadImageViewModel uploadImageViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private LoggedInUserView user;

    private FragmentProfileBinding binding;

    private Uri imageUri;
    private static Bitmap bitmap;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private Observer<GetImageResult> imgObs;
    private GetProfileViewModel getProfileViewModel;
    private ProfileInfoView profileInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        logoutViewModel = new ViewModelProvider(this, new LogoutViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(LogoutViewModel.class);
        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);
        getProfileViewModel = new ViewModelProvider(getActivity()).get(GetProfileViewModel.class);
        downloadImageViewModel = new ViewModelProvider(getActivity()).get(DownloadImageViewModel.class);
        getActivitiesByUserViewModel = new ViewModelProvider(getActivity()).get(GetActivitiesByUserViewModel.class);
        getMyActivitiesViewModel = new ViewModelProvider(getActivity()).get(GetMyActivitiesViewModel.class);

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        getActivitiesByUserViewModel.getActivities(user.getUsername(), user.getTokenID());
        getMyActivitiesViewModel.getActivities(user.getUsername(), user.getTokenID());
        getProfileViewModel.getProfile(user.getUsername(), user.getUsername(), user.getTokenID());

        profileImage = root.findViewById(R.id.profileImage);
        aboutMeTextView = root.findViewById(R.id.aboutMeTextView);
        topNavigationProfile = root.findViewById(R.id.topNavigationProfile);


        loginPreferences = getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        drawerLayout = root.findViewById(R.id.drawerLayout);
        settingsLinearLayout = root.findViewById(R.id.settingsLinearLayout);
        statsLinearLayout = root.findViewById(R.id.statsLinearLayout);
        logoutLinearLayout = root.findViewById(R.id.logoutLinearLayout);
        deleteAccountLinearLayout = root.findViewById(R.id.deleteAccountLinearLayout);

        //TODO: escolher se queremos continuar com o stats
        if (user.getRole() == 0) { //volunteer
            statsLinearLayout.setVisibility(LinearLayout.GONE);
        }

        settingsLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UpdateActivity.class);
                intent.putExtra("UserLogged", user);
                intent.putExtra("ProfileInfo", profileInfo);
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

        deleteAccountLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentPop = new Intent(getActivity(), PopDeleteAccount.class);
                intentPop.putExtra("UserLogged", user);
                startActivity(intentPop);
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(intent.ACTION_GET_CONTENT);

                someActivityResultLauncher.launch(intent);
            }
        });

        getProfileViewModel.getProfileResult().observe(getActivity(), new Observer<GetProfileResult>() {
            @Override
            public void onChanged(GetProfileResult getProfileResult) {
                if (getProfileResult.getError() != null)
                    return;
                if (getProfileResult.getSuccess() != null)
                    return;
            }
        });

        getProfileViewModel.profile().observe(getActivity(), profile -> {
            profileInfo = profile;
            String bio = profileInfo.getBio();
            aboutMeTextView.setText(bio);

            if (profileInfo.getImage() == null)
                downloadImageViewModel.getImage().observe(getViewLifecycleOwner(), image -> {
                    if (image.getObjName().split("_").length == 1) {
                        profileInfo.setImage(image);
                        byte[] img = image.getImageBytes();
                        bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                        profileImage.setImageBitmap(bitmap);
                    }
                });
            else {
                byte[] img = profileInfo.getImage().getImageBytes();
                bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                profileImage.setImageBitmap(bitmap);
            }
        });

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new FutureActivitiesFragment();
        fragmentTransaction.replace(R.id.activities_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        topNavigationProfile.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_future_activities:
                        Fragment fragment = new FutureActivitiesFragment();
                        fragmentTransaction.replace(R.id.activities_layout, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.navigation_enrolled_activities:
                        Fragment afragment = new EnrolledActivitiesFragment();
                        fragmentTransaction.replace(R.id.activities_layout, afragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.navigation_my_activities:
                        Fragment bfragment = new MyActivitiesFragment();
                        fragmentTransaction.replace(R.id.activities_layout, bfragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });
        getActivity().invalidateOptionsMenu();
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_nav_drawer, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT))
            drawerLayout.closeDrawer(Gravity.RIGHT);
        else
            openDrawer(drawerLayout);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(Gravity.RIGHT);
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                            profileImage.setImageBitmap(bitmap);
                            uploadImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
                            try {
                                downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

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

    private void showProfileFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }
}