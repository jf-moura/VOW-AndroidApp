package pt.vow.ui.podium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.cloud.storage.Acl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pt.vow.R;
import pt.vow.data.model.UserInfo;
import pt.vow.databinding.FragmentPodiumBinding;
import pt.vow.ui.getAllUsers.GetAllUsersViewModel;
import pt.vow.ui.image.Image;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.image.DownloadImageViewModel;

public class PodiumFragment extends Fragment {

    private DownloadImageViewModel downloadImageViewModel;
    private FragmentPodiumBinding binding;
    private RecyclerView recyclerView;
    private LoggedInUserView user;
    private GetAllUsersViewModel getAllUsersViewModel;
    private List<UserInfo> usersList;
    private Map<String, Image> images;
    private ShapeableImageView firstPlaceImg, secondPlaceImg, thirdPlaceImg;
    private TextView firstPlaceName, secondPlaceName, thirdPlaceName;
    private ImageView imageViewInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        downloadImageViewModel = new ViewModelProvider(getActivity()).get(DownloadImageViewModel.class);

        binding = FragmentPodiumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");
        getAllUsersViewModel = new ViewModelProvider(getActivity()).get(GetAllUsersViewModel.class);

        images = new HashMap<>();

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        recyclerView = root.findViewById(R.id.activities_recycler_view);
        firstPlaceImg = root.findViewById(R.id.firstPlaceImage);
        secondPlaceImg = root.findViewById(R.id.secondPlaceImage);
        thirdPlaceImg = root.findViewById(R.id.thirdPlaceImage);
        imageViewInfo = root.findViewById(R.id.imageViewInfo);
        firstPlaceName = root.findViewById(R.id.firstPlaceName);
        secondPlaceName = root.findViewById(R.id.secondPlaceName);
        thirdPlaceName = root.findViewById(R.id.thirdPlaceName);

        class SortbyPoints implements Comparator<UserInfo> {
            // Used for sorting in ascending order of
            // score
            public int compare(UserInfo a, UserInfo b) {
                return b.getScore() - a.getScore();
            }
        }

        getAllUsersViewModel.getAllUsersList().observe(getActivity(), users -> {
            usersList = users;
            Collections.sort(usersList, new SortbyPoints());
            PodiumRecyclerViewAdapter adapter = new PodiumRecyclerViewAdapter(getContext(), usersList, user);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            if (users.size() >= 3) {
                firstPlaceName.setText(users.get(0).getName());
                secondPlaceName.setText(users.get(1).getName());
                thirdPlaceName.setText(users.get(2).getName());

                Image userImage = usersList.get(0).getImage();
                if (userImage != null) {
                    byte[] img = userImage.getImageBytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                    firstPlaceImg.setImageBitmap(bitmap);
                } else {
                    try {
                        downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", users.get(0).getUsername());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                userImage = usersList.get(1).getImage();
                if (userImage != null) {
                    byte[] img = userImage.getImageBytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                    secondPlaceImg.setImageBitmap(bitmap);
                } else {
                    try {
                        downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", users.get(1).getUsername());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                userImage = usersList.get(2).getImage();
                if (userImage != null) {
                    byte[] img = userImage.getImageBytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                    thirdPlaceImg.setImageBitmap(bitmap);
                } else {
                    try {
                        downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", users.get(2).getUsername());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        downloadImageViewModel.getImage().observe(getActivity(), image -> {
            String objName = image.getObjName();
            byte[] img = image.getImageBytes();
            Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            if (objName.equals(usersList.get(0).getUsername()))
                firstPlaceImg.setImageBitmap(bitmap);
            else if (objName.equals(usersList.get(1).getUsername()))
                secondPlaceImg.setImageBitmap(bitmap);
            else if (objName.equals(usersList.get(2).getUsername()))
                thirdPlaceImg.setImageBitmap(bitmap);
        });

        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.podium_info, Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        getAllUsersViewModel.getAllUsers(user.getUsername(), user.getTokenID());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
