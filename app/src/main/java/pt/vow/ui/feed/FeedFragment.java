package pt.vow.ui.feed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.FragmentFeedBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.mainPage.DownloadImageViewModel;
import pt.vow.ui.mainPage.GetImageResult;
import pt.vow.ui.maps.MapsFragment;

public class FeedFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Activity> activitiesList;
    private GetActivitiesViewModel activitiesViewModel;
    private LoggedInUserView user;
    private FragmentFeedBinding binding;
    private TextView activitiesTextView;

    private Observer<GetActivitiesResult> actObs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        recyclerView = root.findViewById(R.id.activities_recycler_view);
        activitiesTextView = root.findViewById(R.id.activitiesTextView);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        activitiesViewModel = new ViewModelProvider(requireActivity()).get(GetActivitiesViewModel.class);

        activitiesViewModel.getActivitiesResult().observeForever(actObs = new Observer<GetActivitiesResult>() {
            @Override
            public void onChanged(@Nullable GetActivitiesResult getActivitiesResult) {
                if (getActivitiesResult == null) {
                    return;
                }
                if (getActivitiesResult.getError() != null) {
                    activitiesTextView.setText(R.string.no_activities_available);
                }
                if (getActivitiesResult.getSuccess() != null) {
                    activitiesList = getActivitiesResult.getSuccess().getActivities();
                    if (activitiesList.size() == 0) {
                        activitiesTextView.setText(R.string.no_activities_available);
                    }
                    if (activitiesList != null) {
                        List<Activity> aux = new LinkedList<>();
                        for (Activity a : activitiesList) {
                            long currentTime = Calendar.getInstance().getTimeInMillis();

                            String[] dateTime = a.getTime().split(" ");
                            String[] hours = dateTime[3].split(":");

                            Calendar beginTime = Calendar.getInstance();
                            if (dateTime[4].equals("PM"))
                                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length() - 1)), Integer.valueOf(hours[0]) + 12, Integer.valueOf(hours[1]));
                            else
                                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length() - 1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));

                            long startMillis = beginTime.getTimeInMillis();
                            if (startMillis > currentTime) {
                                aux.add(a);
                            }
                        }

                        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), aux, user);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        activitiesViewModel.getActivities(user.getUsername(), user.getTokenID());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater){
        menuInflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        Fragment fragment = new MapsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.feedFragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setHasOptionsMenu(false);
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (actObs != null)
            activitiesViewModel.getActivitiesResult().removeObserver(actObs);
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