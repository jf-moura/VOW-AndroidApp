package pt.vow.ui.podium;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.R;
import pt.vow.databinding.FragmentPodiumBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.getActivities.DownloadImageViewModel;
import pt.vow.ui.getActivities.DownloadImageViewModelFactory;
import pt.vow.ui.getActivities.GetImageResult;
import pt.vow.ui.login.LoggedInUserView;

public class PodiumFragment extends Fragment {

    private PodiumViewModel podiumViewModel;
    private FragmentPodiumBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        podiumViewModel =
                new ViewModelProvider(this).get(PodiumViewModel.class);

        binding = FragmentPodiumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
