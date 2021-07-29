package pt.vow.ui.feed;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import pt.vow.data.model.UserInfo;
import pt.vow.databinding.FragmentFeedBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModel;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModelFactory;
import pt.vow.ui.getAllUsers.GetAllUsersViewModel;
import pt.vow.ui.getAllUsers.GetAllUsersViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.image.DownloadImageViewModel;
import pt.vow.ui.maps.MapsFragment;
import pt.vow.ui.profile.ActivitiesByUserView;
import pt.vow.ui.profile.GetActivitiesByUserViewModel;


public class FeedFragment extends Fragment {
    private FeedFragment mActivity;
    private GetActivitiesViewModel activitiesViewModel;
    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private LoggedInUserView user;
    private FragmentFeedBinding binding;
    private RecyclerViewAdapter adapter;
    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    private ActivitiesByUserView enrolledActivities;
    private List<Activity> activityList, activityListToMap;
    private Map<String, Activity> aux;

    private RecyclerView recyclerView;
    private TextView activitiesTextView;
    private AutoCompleteTextView searchView;
    private AutoCompleteTextView autoCompleteTextView;
    private GetAllUsersViewModel getAllUsersViewModel;
    private GetFiveActivitiesViewModel getFiveActivitiesViewModel;
    private List<UserInfo> users;
    private String[] auxSearchView;
    private String input;
    private ActivityParticipantsViewModel actParticipantsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");
        input = "";
        mActivity = this;

        recyclerView = root.findViewById(R.id.activities_recycler_view);
        activitiesTextView = root.findViewById(R.id.activitiesTextView);
        autoCompleteTextView = root.findViewById(R.id.autoCompleteTextView2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.filter, R.layout.dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        autoCompleteTextView.setAdapter(adapter);
        searchView = root.findViewById(R.id.search_org);
        getAllUsersViewModel = new ViewModelProvider(this, new GetAllUsersViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(GetAllUsersViewModel.class);
        getFiveActivitiesViewModel = new ViewModelProvider(this, new GetFiveActivitiesViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(GetFiveActivitiesViewModel.class);

        activitiesViewModel = new ViewModelProvider(this, new GetActivitiesViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(GetActivitiesViewModel.class);

        getAllUsersViewModel.getAllUsers(user.getUsername(), user.getTokenID());

        getAllUsersViewModel.getAllUsersList().observe(getViewLifecycleOwner(), usersList -> {
            users = usersList;
            auxSearchView = new String[usersList.size()];
            int count = 0;
            for (UserInfo u : users) {
                auxSearchView[count++] = u.getUsername();
            }
            ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, auxSearchView);
            searchView.setAdapter(adapterSearch);

            searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    input = searchView.getText().toString();
                    if (!input.isEmpty()) {
                        List<Activity> aux = new LinkedList<>();
                        for (Activity a : activityList) {
                            if (a.getOwner().equals(input))
                                aux.add(a);
                        }
                        RecyclerViewAdapter adapter1 = new RecyclerViewAdapter(getContext(), aux, user, enrolledActivities);
                        recyclerView.setAdapter(adapter1);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                    input = "";
                }
            });
        });

        recyclerView = root.findViewById(R.id.activities_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page + 1);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
        loadNextDataFromApi(1);

        setHasOptionsMenu(true);
        return root;
    }

    // Append the next page of data into the adapter
    // This method probably sends out a network request and appends new data items to your adapter.
    private void loadNextDataFromApi(int offset) {
        getFiveActivitiesViewModel.getActivites(user.getUsername(), user.getTokenID(), offset);

        getFiveActivitiesViewModel.getActivitiesResult().observe(getActivity(), getFiveActivitiesResult -> {
            if (getFiveActivitiesResult == null)
                return;
            if (getFiveActivitiesResult.getError() != null) {
                Toast.makeText(getActivity().getApplicationContext(), getFiveActivitiesResult.getError(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (getFiveActivitiesResult.getSuccess() != null) {
                List<Activity> curActivities = getFiveActivitiesResult.getSuccess().getActivities();
                if (curActivities.size() == 0)
                    activitiesTextView.setText(R.string.no_activities_available);
                else if (curActivities != null) {
                    aux = new HashMap<>();
                    for (Activity a : curActivities) {
                        if (a.getStatus()) {
                            aux.put(a.getId(), a);

                            if (a.getParticipants() == null)
                                actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), a.getOwner(), a.getId());
                        }
                    }
                    activityList = new ArrayList<>(aux.values());
                    showFilteredAct(activityList);

                    adapter = new RecyclerViewAdapter(getContext(), activityList, user, enrolledActivities);
                    adapter.notifyItemRangeInserted(offset, 5);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
        // Send an API request to retrieve appropriate paginated data
        //  --> Send the request including an offset value (i.e `page`) as a query parameter.
        //  --> Deserialize and construct new model objects from the API response
        //  --> Append the new data objects to the existing set of items inside the array of items
        //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivitiesByUserViewModel = new ViewModelProvider(getActivity()).get(GetActivitiesByUserViewModel.class);
        //activitiesViewModel = new ViewModelProvider(getActivity()).get(GetActivitiesViewModel.class);

        activitiesViewModel.getActivities(user.getUsername(), user.getTokenID());

        getActivitiesByUserViewModel.getActivitiesList().observe(this, activitiesByUser -> {
            enrolledActivities = activitiesByUser;
        });

        actParticipantsViewModel = new ViewModelProvider(this, new ActivityParticipantsViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(ActivityParticipantsViewModel.class);

        activitiesViewModel.getActivitiesList().observe(getActivity(), activities -> {
            if (activityList != activities) {
                activityListToMap = activities;

                if (activities != null) {
                    Map<String, Activity> aux2 = new HashMap<>();
                    for (Activity a : activities) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();

                        String[] dateTime = a.getTime().split(" ");
                        String[] hours = dateTime[3].split(":");

                        Calendar beginTime = Calendar.getInstance();

                        beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length() - 1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));

                        long startMillis = beginTime.getTimeInMillis();
                        if (startMillis > currentTime) {
                            aux2.put(a.getId(), a);

                            if (a.getParticipants() == null)
                                actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), a.getOwner(), a.getId());
                        }
                    }
                    activityListToMap = new ArrayList<>(aux2.values());
                    /*showFilteredAct(activityList);

                    adapter = new RecyclerViewAdapter(getContext(), activityList, user, enrolledActivities);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));*/

                }
            }
        });

        this.onCompleteIsEmpty();

        actParticipantsViewModel.getParticipantsList().observe(this, participants -> {
            Activity a = aux.get(participants.getActivityID());
            if (a != null)
                a.addParticipants(participants.getParticipants());
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
        bundle.putSerializable("Activities", (Serializable) activityListToMap);
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

    private void onCompleteIsEmpty() {
        searchView.addTextChangedListener(new TextWatcher() {
            String afterTextChanged = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                afterTextChanged = searchView.getText().toString();
                if (afterTextChanged.isEmpty()) {
                    recyclerView.setAdapter(adapter);
                }
            }
        });
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
                adapter = new RecyclerViewAdapter(getContext(), filter, user, enrolledActivities);
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