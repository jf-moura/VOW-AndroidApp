package pt.vow.ui.podium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.io.IOException;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.data.model.UserInfo;
import pt.vow.databinding.FragmentPodiumBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.GetRatingViewModel;
import pt.vow.ui.activityInfo.GetRatingViewModelFactory;
import pt.vow.ui.feed.GetActivitiesViewModel;
import pt.vow.ui.feed.RecyclerViewAdapter;
import pt.vow.ui.getAllUsers.GetAllUsersViewModel;
import pt.vow.ui.getAllUsers.GetAllUsersViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.mainPage.DownloadImageViewModel;
import pt.vow.ui.mainPage.Image;
import pt.vow.ui.mainPage.ImagesViewModel;

public class PodiumFragment extends Fragment {

    private DownloadImageViewModel downloadImageViewModel;
    private ImagesViewModel imagesViewModel;
    private FragmentPodiumBinding binding;
    private RecyclerView recyclerView;
    private LoggedInUserView user;
    private GetAllUsersViewModel getAllUsersViewModel;
    private List<UserInfo> usersList;
    private ShapeableImageView firstPlaceImg, secondPlaceImg, thirdPlaceImg;
    private ImageView imageViewInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        downloadImageViewModel = new ViewModelProvider(getActivity()).get(DownloadImageViewModel.class);
        imagesViewModel = new ViewModelProvider(getActivity()).get(ImagesViewModel.class);

        binding = FragmentPodiumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");
        getAllUsersViewModel = new ViewModelProvider(getActivity()).get(GetAllUsersViewModel.class);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        recyclerView = root.findViewById(R.id.activities_recycler_view);
        firstPlaceImg = root.findViewById(R.id.firstPlaceImage);
        secondPlaceImg = root.findViewById(R.id.secondPlaceImage);
        thirdPlaceImg = root.findViewById(R.id.thirdPlaceImage);
        imageViewInfo = root.findViewById(R.id.imageViewInfo);

        getAllUsersViewModel.getAllUsers(user.getUsername(), user.getTokenID());

        getAllUsersViewModel.getAllUsersList().observe(getActivity(), users -> {
            usersList = users;
            if (usersList.size() >= 3) {
                for (int i = 0; i < 3; i++) {
                    Image image = imagesViewModel.getImage(usersList.get(i).getUsername());
                    if (image != null) {
                        byte[] img = image.getImageBytes();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                        firstPlaceImg.setImageBitmap(bitmap);
                    } else {
                        try {
                            downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", usersList.get(i).getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            PodiumRecyclerViewAdapter adapter = new PodiumRecyclerViewAdapter(getContext(), usersList, user);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        });

        downloadImageViewModel.getImage().observe(getActivity(), image -> {
            if (usersList != null) {
                byte[] img = image.getImageBytes();
                Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
                String imgOwner = image.getObjName();
                if (imgOwner.equals(usersList.get(0).getUsername()))
                    firstPlaceImg.setImageBitmap(bitmap);
                else if (imgOwner.equals(usersList.get(1).getUsername()))
                    secondPlaceImg.setImageBitmap(bitmap);
                else if (imgOwner.equals(usersList.get(2).getUsername()))
                    thirdPlaceImg.setImageBitmap(bitmap);
            }
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
    public void onDestroyView() {
        super.onDestroyView();
    }
}
