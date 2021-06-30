package pt.vow.ui.feed;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.FragmentFeedBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.getActivities.GetActivitiesViewModel;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.maps.MapsFragment;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Activity> activitiesList;
    private GetActivitiesViewModel activitiesViewModel;
    private FeedViewModel feedViewModel;
    private LoggedInUserView user;
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
        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        recyclerView = root.findViewById(R.id.activities_recycler_view);

        if (activitiesList != null) {
            List<Activity> aux = new LinkedList<>();
            for (Activity a : activitiesList) {
                Calendar currentTime = Calendar.getInstance();

                String[] dateTime = a.getTime().split(" ");
                String[] hours = dateTime[3].split(":");

                Calendar beginTime = Calendar.getInstance();
                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));
                long startMillis = beginTime.getTimeInMillis();
                if (startMillis > currentTime.getTimeInMillis()) {
                    aux.add(a);
                }
            }

            RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), aux, user);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_feed:
                Intent intent = new Intent(getActivity(), MapsFragment.class);
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private int monthToIntegerShort(String month) {
        int result = 0;
        switch (month) {
            case "Jan":
                result = 0;
                break;
            case "Fev":
                result = 1;
                break;
            case "Mar":
                result = 2;
                break;
            case "Apr":
                result = 3;
                break;
            case "May":
                result = 4;
                break;
            case "Jun":
                result = 5;
                break;
            case "Jul":
                result = 6;
                break;
            case "Aug":
                result = 7;
                break;
            case "Sep":
                result = 8;
                break;
            case "Oct":
                result = 9;
                break;
            case "Nov":
                result = 10;
                break;
            case "Dec":
                result = 11;
                break;
        }
        return result;
    }
}