package pt.vow.ui.podium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import pt.vow.databinding.FragmentPodiumBinding;

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
