package pt.vow.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.ScrollviewActivitiesBinding;
import pt.vow.ui.login.LoggedInUserView;

public class FutureActivitiesFragment extends Fragment {
    private ScrollviewActivitiesBinding binding;

    private RecyclerView enrolledActRecyclerView;

    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private LoggedInUserView user;

    private List<Activity> activitiesByUserList;

    private Observer<GetActivitiesByUserResult> actByUserObs;

    private RelativeLayout relativeLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = ScrollviewActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        enrolledActRecyclerView = root.findViewById(R.id.activities_recycler_view_profile);
        relativeLayout = root.findViewById(R.id.empty_state);

        //if (user.getRole() == 0) { // volunteer
        getActivitiesByUserViewModel = new ViewModelProvider(requireActivity()).get(GetActivitiesByUserViewModel.class);
        getActivitiesByUserViewModel.getActivitiesResult().observeForever(actByUserObs = new Observer<GetActivitiesByUserResult>() {
            @Override
            public void onChanged(@Nullable GetActivitiesByUserResult getActivitiesResult) {
                if (getActivitiesResult == null) {
                    return;
                }
                if (getActivitiesResult.getError() != null) {
                    showGetActivitiesFailed(getActivitiesResult.getError());
                }
                if (getActivitiesResult.getSuccess() != null) {
                    activitiesByUserList = getActivitiesResult.getSuccess().getActivities();
                    if (activitiesByUserList.size() == 0) {
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                    if (activitiesByUserList != null) {
                        List<Activity> aux = new LinkedList<>();
                        for (Activity a : activitiesByUserList) {
                            Calendar currentTime = Calendar.getInstance();

                            String[] dateTime = a.getTime().split(" ");
                            String[] hours = dateTime[3].split(":");

                            Calendar beginTime = Calendar.getInstance();;
                            if (dateTime[4].equals("PM"))
                                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]) + 12, Integer.valueOf(hours[1]));
                            else
                                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]), Integer.valueOf(hours[1]));

                            long startMillis = beginTime.getTimeInMillis();
                            if(startMillis >= currentTime.getTimeInMillis()){
                                aux.add(a);
                            }
                        }
                        if (aux.isEmpty())
                            relativeLayout.setVisibility(View.VISIBLE);
                        else {
                            ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(getContext(), aux, user);
                            enrolledActRecyclerView.setAdapter(adapter);
                            enrolledActRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    }

                }
            }
        });
        //}

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        //if (user.getRole() == 0) // volunteer
        getActivitiesByUserViewModel.getActivities(user.getUsername(), user.getTokenID());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (actByUserObs != null)
            getActivitiesByUserViewModel.getActivitiesResult().removeObserver(actByUserObs);
    }

    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
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
