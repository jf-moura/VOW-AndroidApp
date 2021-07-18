package pt.vow.ui.feed;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.FragmentFeedBinding;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.mainPage.DownloadImageViewModel;
import pt.vow.ui.mainPage.Image;
import pt.vow.ui.maps.MapsFragment;
import pt.vow.ui.profile.ActivitiesByUserView;
import pt.vow.ui.profile.GetActivitiesByUserViewModel;

public class FeedFragment extends Fragment {
    private FeedFragment mActivity;
    private GetActivitiesViewModel activitiesViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private LoggedInUserView user;
    private FragmentFeedBinding binding;

    private Map<String, Activity> aux;
    private ActivitiesByUserView enrolledActivities;
    private List<Activity> activityList;

    private RecyclerView recyclerView;
    private TextView activitiesTextView;
    private AutoCompleteTextView autoCompleteTextView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        mActivity = this;

        recyclerView = root.findViewById(R.id.activities_recycler_view);
        activitiesTextView = root.findViewById(R.id.activitiesTextView);

        autoCompleteTextView = root.findViewById(R.id.autoCompleteTextView2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.filter, R.layout.dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        autoCompleteTextView.setAdapter(adapter);

        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        downloadImageViewModel = new ViewModelProvider(getActivity()).get(DownloadImageViewModel.class);
        getActivitiesByUserViewModel = new ViewModelProvider(getActivity()).get(GetActivitiesByUserViewModel.class);
        enrolledActivities = getActivitiesByUserViewModel.getActivitiesList().getValue();
        activitiesViewModel = new ViewModelProvider(getActivity()).get(GetActivitiesViewModel.class);
        activitiesViewModel.getActivities(user.getUsername(), user.getTokenID());

        activitiesViewModel.getActivitiesList().observe(getActivity(), activities -> {
            activityList = activities;
            if (activities.size() == 0)
                activitiesTextView.setText(R.string.no_activities_available);
            else if (activities != null) {
                aux = new HashMap<>();
                for (Activity a : activities) {

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
                        aux.put(a.getOwner() + "_" + a.getName(), a);
                        try {
                            downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", a.getOwner() + "_" + a.getName());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                activityList = new ArrayList<>(aux.values());
                showFilteredAct(activityList);

                RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), activityList, user, enrolledActivities);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });

        downloadImageViewModel.getImage().observe(getActivity(), image -> {
            if (image.getObjName().split("_").length == 2) {
                String objName = image.getObjName();
                aux.get(objName).setImage(image);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.menu_map, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("EnrolledActivities", enrolledActivities);
        bundle.putSerializable("Activities", (Serializable) activityList);
        Fragment fragment = new MapsFragment();
        fragment.setArguments(bundle);
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
    }

    private void showFilteredAct(List<Activity> aux) {
        List<Activity> filter = new LinkedList<>();
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                filter.clear();
                switch (position) {
                    case 0:
                        for (Activity act : aux) {
                            if (act.getType().equals("health"))
                                filter.add(act);
                        }
                        break;
                    case 1:
                        for (Activity act : aux) {
                            if (act.getType().equals("children"))
                                filter.add(act);
                        }
                        break;
                    case 2:
                        for (Activity act : aux) {
                            if (act.getType().equals("nature"))
                                filter.add(act);
                        }
                        break;
                    case 3:
                        for (Activity act : aux) {
                            if (act.getType().equals("houseBuilding"))
                                filter.add(act);
                        }
                        break;
                    case 4:
                        for (Activity act : aux) {
                            if (act.getType().equals("elderly"))
                                filter.add(act);
                        }
                        break;
                    case 5:
                        for (Activity act : aux) {
                            if (act.getType().equals("animals"))
                                filter.add(act);
                        }
                        break;
                    case 6:
                        for (Activity act : aux) {
                            filter.add(act);
                        }
                        break;
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), filter, user, enrolledActivities);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        });
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