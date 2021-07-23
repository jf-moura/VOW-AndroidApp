package pt.vow.ui.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import pt.vow.ui.VOW;
import pt.vow.ui.image.UploadImageViewModel;
import pt.vow.ui.image.UploadImageViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.logout.LogoutViewModel;
import pt.vow.ui.logout.LogoutViewModelFactory;
import pt.vow.ui.image.DownloadImageViewModel;
import pt.vow.ui.image.DownloadImageViewModelFactory;
import pt.vow.ui.image.GetImageResult;
import pt.vow.ui.image.Image;

public class ShowProfileActivity extends AppCompatActivity {
    private static final int RESULT_OK = -1;

    private ShapeableImageView profileImage;
    private TextView aboutMeTextView, textAccPrivate;
    private Switch switchMode;
    private boolean mode;
    private BottomNavigationView topNavigationProfile;
    private ImageView imageLock;

    private LogoutViewModel logoutViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private UploadImageViewModel uploadImageViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private LoggedInUserView user;
    private Image image;
    private ProfileInfoView profileInfo;

    private Uri imageUri;
    private static Bitmap bitmap;

    private Observer<GetImageResult> imgObs;
    private GetProfileViewModel getProfileViewModel;
    private RelativeLayout relativeLayoutCam;
    private LinearLayout linearLayoutAccP;
    private ShowProfileActivity mActivity;
    private String userToGet;
    private Boolean userVisibility;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mActivity = this;

        logoutViewModel = new ViewModelProvider(this, new LogoutViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(LogoutViewModel.class);
        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);

        getProfileViewModel = new ViewModelProvider(this, new GetProfileViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetProfileViewModel.class);
        downloadImageViewModel = new ViewModelProvider(this, new DownloadImageViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(DownloadImageViewModel.class);
        getActivitiesByUserViewModel = new ViewModelProvider(this, new GetActivitiesByUserViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetActivitiesByUserViewModel.class);
        getMyActivitiesViewModel = new ViewModelProvider(this, new GetMyActivitiesViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(GetMyActivitiesViewModel.class);

        // Bundle bundle = this.getArguments();
        // user = (LoggedInUserView) bundle.getSerializable("UserLogged");
        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        userToGet = (String) getIntent().getSerializableExtra("UserShown");
        userVisibility = (Boolean) getIntent().getSerializableExtra("UserShownVisibility");


        profileImage = findViewById(R.id.profileImage);
        aboutMeTextView = findViewById(R.id.aboutMeTextView);
        topNavigationProfile = findViewById(R.id.topNavigationProfile);

        relativeLayoutCam = findViewById(R.id.relativeLayoutCam);
        relativeLayoutCam.setVisibility(View.INVISIBLE);
        imageLock = findViewById(R.id.imageLock);
        textAccPrivate = findViewById(R.id.textAccPrivate);
        linearLayoutAccP = findViewById(R.id.linearLayoutAccP);

        //public account
        if (userVisibility) {
            getActivitiesByUserViewModel.getActivities(userToGet, user.getUsername(), user.getTokenID());
            getMyActivitiesViewModel.getActivities(userToGet, user.getUsername(), user.getTokenID());
        } else {
            linearLayoutAccP.setVisibility(View.VISIBLE);
          //  imageLock.setVisibility(View.VISIBLE);
          //  textAccPrivate.setVisibility(View.VISIBLE);
            topNavigationProfile.setVisibility(View.GONE);
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);// set drawable icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (profileInfo == null) {
            getProfileViewModel.getProfile(userToGet, user.getUsername(), user.getTokenID());
            getProfileViewModel.profile().observe(this, profile -> {
                profileInfo = profile;
                getSupportActionBar().setTitle(profileInfo.getUsername());
                String bio = profileInfo.getBio();
                aboutMeTextView.setText(bio);
            });
        }


        if (image == null) {
            try {
                downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", userToGet);
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadImageViewModel.getImage().observe(this, images -> {
               image = images;
               byte[] img = images.getImageBytes();
               bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
               profileImage.setImageBitmap(bitmap);
            });
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new EnrolledActivitiesFragment();
        fragmentTransaction.replace(R.id.activities_layout2, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        topNavigationProfile.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_enrolled_activities:
                        Fragment afragment = new EnrolledActivitiesFragment();
                        fragmentTransaction.replace(R.id.activities_layout2, afragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.navigation_my_activities:
                        Fragment bfragment = new MyActivitiesFragment();
                        fragmentTransaction.replace(R.id.activities_layout2, bfragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });

      //  this.invalidateOptionsMenu();
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
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}