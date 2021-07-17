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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pt.vow.R;
import pt.vow.databinding.FragmentPodiumBinding;
import pt.vow.ui.feed.RecyclerViewAdapter;

public class PodiumFragment extends Fragment {

    private PodiumViewModel podiumViewModel;
    private FragmentPodiumBinding binding;
    private RecyclerView recyclerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        podiumViewModel =
                new ViewModelProvider(this).get(PodiumViewModel.class);

        binding = FragmentPodiumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        recyclerView = root.findViewById(R.id.activities_recycler_view);

       /* RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), list, user);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
