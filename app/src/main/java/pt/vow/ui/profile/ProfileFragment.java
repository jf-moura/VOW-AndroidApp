package pt.vow.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;

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

    private ImageButton profileImage;
    private EditText aboutMeEditText;
    private LinearLayout settingsLinearLayout, statsLinearLayout, logoutLinearLayout, linearLayoutPrincipal;
    private DrawerLayout drawerLayout;
    private BottomNavigationView topNavigationProfile;

    private LogoutViewModel logoutViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private UploadImageViewModel uploadImageViewModel;
    private LoggedInUserView user;

    private FragmentProfileBinding binding;

    private Uri imageUri;
    private static Bitmap bitmap;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    private Observer<GetImageResult> imgObs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        logoutViewModel = new ViewModelProvider(this, new LogoutViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(LogoutViewModel.class);
        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        profileImage = root.findViewById(R.id.profileImage);
        aboutMeEditText = root.findViewById(R.id.aboutMeEditText);
        topNavigationProfile = root.findViewById(R.id.topNavigationProfile);

        loginPreferences = getContext().getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        drawerLayout = root.findViewById(R.id.drawerLayout);
        settingsLinearLayout = root.findViewById(R.id.settingsLinearLayout);
        statsLinearLayout = root.findViewById(R.id.statsLinearLayout);
        logoutLinearLayout = root.findViewById(R.id.logoutLinearLayout);
        linearLayoutPrincipal = root.findViewById(R.id.linearLayoutPrincipal);


        if (user.getRole() == 0) { //volunteer
            statsLinearLayout.setVisibility(LinearLayout.GONE);
            ViewGroup.LayoutParams params = linearLayoutPrincipal.getLayoutParams();
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
            params.height = height;
            linearLayoutPrincipal.setLayoutParams(params);
        }

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

                someActivityResultLauncher.launch(intent);
            }
        });

        aboutMeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutMeEditText.setText(aboutMeEditText.getText().toString());
            }
        });

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = new EnrolledActivitiesFragment();
        fragmentTransaction.replace(R.id.drawerLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        topNavigationProfile.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_enrolled_activities:
                        Fragment fragment = new EnrolledActivitiesFragment();
                        fragmentTransaction.replace(R.id.drawerLayout, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                    case R.id.navigation_my_activities:
                        Fragment afragment = new MyActivitiesFragment();
                        fragmentTransaction.replace(R.id.drawerLayout, afragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });


        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        downloadImageViewModel = new ViewModelProvider(requireActivity()).get(DownloadImageViewModel.class);
        downloadImageViewModel.getDownloadResult().observeForever(imgObs = new Observer<GetImageResult>() {
            @Override
            public void onChanged(@Nullable GetImageResult downloadResult) {
                if (downloadResult == null) {
                    return;
                }
                //if (downloadResult.getError() != null) {
                // TODO:
                //}
                if (downloadResult.getSuccess() != null) {
                    byte[] img = downloadResult.getSuccess().getImage();
                    bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                    profileImage.setImageBitmap(bitmap);
                }
            }
        });

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
        if (imgObs != null)
            downloadImageViewModel.getDownloadResult().removeObserver(imgObs);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", user.getUsername());
        } catch (IOException e) {
            e.printStackTrace();
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
    private void uploadImage(String projectId, String bucketName, String objectName) throws IOException {
        if (imageUri != null) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] imageInByte = out.toByteArray();
            out.close();
            uploadImageViewModel.uploadImage(projectId, bucketName, objectName, imageInByte);
        }
    }



}