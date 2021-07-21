package pt.vow.ui.profile;

import android.app.Activity;
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import androidx.navigation.NavArgument;
import androidx.navigation.NavHostController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import pt.vow.R;

import pt.vow.databinding.FragmentProfileBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModel;
import pt.vow.ui.activityInfo.GetRatingViewModel;
import pt.vow.ui.activityInfo.GetRatingViewModelFactory;
import pt.vow.ui.comments.GetActCommentsViewModel;
import pt.vow.ui.comments.GetActCommentsViewModelFactory;
import pt.vow.ui.frontPage.FrontPageActivity;
import pt.vow.ui.login.LoginActivity;
import pt.vow.ui.mainPage.DownloadImageViewModel;
import pt.vow.ui.mainPage.GetImageResult;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.logout.LogoutViewModel;
import pt.vow.ui.logout.LogoutViewModelFactory;
import pt.vow.ui.mainPage.Image;
import pt.vow.ui.update.ChangeVisibilityResult;
import pt.vow.ui.update.ChangeVisibilityViewModel;
import pt.vow.ui.update.ChangeVisibilityViewModelFactory;
import pt.vow.ui.update.UpdateActivity;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {
    private static final int RESULT_OK = -1;

    private ShapeableImageView profileImage;
    private TextView aboutMeTextView, textAccPrivate;
    private LinearLayout settingsLinearLayout, statsLinearLayout, logoutLinearLayout, deleteAccountLinearLayout;
    private Switch switchMode;
    private boolean mode;
    private DrawerLayout drawerLayout;
    private BottomNavigationView topNavigationProfile;
    private ImageView imageLock;

    private LogoutViewModel logoutViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private UploadImageViewModel uploadImageViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private ChangeVisibilityViewModel changeVisibilityViewModel;
    private LoggedInUserView user;
    private Image image;
    private ProfileInfoView profileInfo;

    private FragmentProfileBinding binding;

    private Uri imageUri;
    private static Bitmap bitmap;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private Observer<GetImageResult> imgObs;
    private GetProfileViewModel getProfileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        logoutViewModel = new ViewModelProvider(this, new LogoutViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(LogoutViewModel.class);
        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);
        changeVisibilityViewModel = new ViewModelProvider(this, new ChangeVisibilityViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(ChangeVisibilityViewModel.class);

        getProfileViewModel = new ViewModelProvider(getActivity()).get(GetProfileViewModel.class);
        downloadImageViewModel = new ViewModelProvider(getActivity()).get(DownloadImageViewModel.class);
        getActivitiesByUserViewModel = new ViewModelProvider(getActivity()).get(GetActivitiesByUserViewModel.class);
        getMyActivitiesViewModel = new ViewModelProvider(getActivity()).get(GetMyActivitiesViewModel.class);

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        getActivitiesByUserViewModel.getActivities(user.getUsername(), user.getUsername(), user.getTokenID());
        getMyActivitiesViewModel.getActivities(user.getUsername(), user.getUsername(), user.getTokenID());

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
        switchMode = root.findViewById(R.id.switch2);
        imageLock = root.findViewById(R.id.imageLock);
        textAccPrivate = root.findViewById(R.id.textAccPrivate);


        this.getProfileInfo();

        //TODO: escolher se queremos continuar com o stats
        if (user.getRole() == 0) { //volunteer
            statsLinearLayout.setVisibility(LinearLayout.GONE);
        }

        if (profileInfo != null) {
            mode = profileInfo.getVisibility();
            switchMode.setChecked(mode);
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
                Intent intent;
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

        switchMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeVisibilityViewModel.changeVisibility(user.getUsername(), user.getTokenID(), user.getUsername(), isChecked);
            }
        });

        changeVisibilityViewModel.getVisibilityChangeResult().observe(getViewLifecycleOwner(), new Observer<ChangeVisibilityResult>() {
            @Override
            public void onChanged(ChangeVisibilityResult changeVisibilityResult) {
                if (changeVisibilityResult == null) {
                    return;
                }
                if (changeVisibilityResult.getError() != null) {
                    showMessage(changeVisibilityResult.getError());
                }
                if (changeVisibilityResult.getSuccess() != null) {
                    mode = changeVisibilityResult.getSuccess().getVisibility();
                    switchMode.setChecked(mode);
                }
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

        if (profileInfo == null) {
            getProfileViewModel.getProfile(user.getUsername(), user.getUsername(), user.getTokenID());
            getProfileViewModel.profile().observe(getActivity(), profile -> {
                profileInfo = profile;
                String bio = profileInfo.getBio();
                aboutMeTextView.setText(bio);
                mode = profileInfo.getVisibility();
                switchMode.setChecked(mode);
            });
        }


        if (image == null) {
            try {
                downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadImageViewModel.getImage().observe(getViewLifecycleOwner(), images -> {
                if (images.getObjName().split("_").length == 1) {
                    image = images;
                    byte[] img = images.getImageBytes();
                    bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                    profileImage.setImageBitmap(bitmap);
                }
            });
        }


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

    private void getProfileInfo() {
        NavArgument args = NavHostFragment.findNavController(this).getCurrentDestination().getArguments().get("Profile");
        if (args != null) {
            Bundle bundle = (Bundle) args.getDefaultValue();

            profileInfo = (ProfileInfoView) bundle.getSerializable("ProfileInfo");
            if (profileInfo != null)
                aboutMeTextView.setText(profileInfo.getBio());

            image = (Image) bundle.getSerializable("ProfileImage");
            if (image != null) {
                byte[] profileImageInByte = image.getImageBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(profileImageInByte, 0, profileImageInByte.length);
                profileImage.setImageBitmap(bitmap);
            }
        }
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
    private void uploadImage(String projectId, String bucketName, String objectName) throws
            IOException {
        if (imageUri != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] imageInByte = out.toByteArray();
            out.close();
            uploadImageViewModel.uploadImage(projectId, bucketName, objectName, imageInByte);
        }
    }

    private void showMessage(@StringRes Integer message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}