package pt.vow.ui.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.FragmentFeedBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.getActivities.ActivitiesRegisteredView;
import pt.vow.ui.getActivities.GetActivitiesViewModel;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Activity> activitiesList;
    private GetActivitiesViewModel activitiesViewModel;
    private FeedViewModel feedViewModel;
    private FragmentFeedBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);
        activitiesViewModel = new ViewModelProvider(requireActivity()).get(GetActivitiesViewModel.class);
        activitiesViewModel.getActivitiesList().observe(getActivity(), list -> {
            activitiesList = list;
        });

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        feedViewModel = new ViewModelProvider(this, new FeedViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(FeedViewModel.class);

        recyclerView = root.findViewById(R.id.activities_recycler_view);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), activitiesList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
