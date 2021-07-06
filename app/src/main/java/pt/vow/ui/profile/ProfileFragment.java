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
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import pt.vow.R;

import pt.vow.data.model.Activity;
import pt.vow.databinding.FragmentProfileBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.extraInfo.UploadImageViewModel;
import pt.vow.ui.extraInfo.UploadImageViewModelFactory;
import pt.vow.ui.frontPage.FrontPageActivity;
import pt.vow.ui.MainPage.DownloadImageViewModel;
import pt.vow.ui.MainPage.GetImageResult;
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
    private TextView myActivitiesTextView;
    private DrawerLayout drawerLayout;

    private ProfileViewModel profileViewModel;
    private LogoutViewModel logoutViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private UploadImageViewModel uploadImageViewModel;
    private LoggedInUserView user;

    private RecyclerView recyclerView;
    private FragmentProfileBinding binding;

    private List<Activity> activitiesByUserList;
    private List<Activity> myActivitiesList;
    private Uri imageUri;
    private static Bitmap bitmap;

    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private boolean saveLogin;

    private Observer<GetImageResult> imgObs;
    private Observer<GetActivitiesByUserResult> actByUserObs;
    private Observer<GetMyActivitiesResult> myActObs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        logoutViewModel = new ViewModelProvider(this, new LogoutViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(LogoutViewModel.class);
        uploadImageViewModel = new ViewModelProvider(this, new UploadImageViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(UploadImageViewModel.class);

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        recyclerView = root.findViewById(R.id.activities_recycler_view_profile);
        profileImage = root.findViewById(R.id.profileImage);
        myActivitiesTextView = root.findViewById(R.id.myActivitiesTextView);
        aboutMeEditText = root.findViewById(R.id.aboutMeEditText);

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

        if (user.getRole() == 0) {
            getActivitiesByUserViewModel = new ViewModelProvider(requireActivity()).get(GetActivitiesByUserViewModel.class);
            getActivitiesByUserViewModel.getActivitiesResult().observeForever(actByUserObs = new Observer<GetActivitiesByUserResult>() {
                @Override
                public void onChanged(@Nullable GetActivitiesByUserResult getActivitiesResult) {
                    if (getActivitiesResult == null) {
                        return;
                    }
                    if (getActivitiesResult.getError() != null) {
                        showGetActivitiesFailed(getActivitiesResult.getError());
                    }
                    if (getActivitiesResult.getSuccess() != null) {
                        activitiesByUserList = getActivitiesResult.getSuccess().getActivities();
                        if (activitiesByUserList.size() == 0) {
                            myActivitiesTextView.setText(R.string.no_activities_available);
                        }
                        if (activitiesByUserList != null) {
                            ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(getContext(), activitiesByUserList);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }

                        getActivity().setResult(android.app.Activity.RESULT_OK);
                    }
                }
            });
        }

        getMyActivitiesViewModel = new ViewModelProvider(requireActivity()).get(GetMyActivitiesViewModel.class);
        getMyActivitiesViewModel.getActivitiesResult().observeForever(myActObs = new Observer<GetMyActivitiesResult>() {
            @Override
            public void onChanged(@Nullable GetMyActivitiesResult getActivitiesResult) {
                if (getActivitiesResult == null) {
                    return;
                }
                if (getActivitiesResult.getError() != null) {
                    showGetActivitiesFailed(getActivitiesResult.getError());
                }
                if (getActivitiesResult.getSuccess() != null) {
                    myActivitiesList = getActivitiesResult.getSuccess().getActivities();
                    if (myActivitiesList.size() == 0) {
                        myActivitiesTextView.setText(R.string.no_activities_available);
                    }
                    if (myActivitiesList != null) {
                        ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(getContext(), myActivitiesList);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }

                    getActivity().setResult(android.app.Activity.RESULT_OK);
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
        if (actByUserObs != null)
        getActivitiesByUserViewModel.getActivitiesResult().removeObserver(actByUserObs);
        if (myActObs != null)
        getMyActivitiesViewModel.getActivitiesResult().removeObserver(myActObs);
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

    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}