package pt.vow.ui.profile;

import android.os.Bundle;
import android.telephony.CarrierConfigManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.FragmentProfileBinding;
import pt.vow.databinding.ScrollviewActivitiesBinding;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.mainPage.DownloadImageViewModel;

public class EnrolledActivitiesFragment extends Fragment {

    private ScrollviewActivitiesBinding binding;

    private RecyclerView enrolledActRecyclerView;

    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private DownloadImageViewModel downloadImageViewModel;
    private LoggedInUserView user;

    private List<Activity> activitiesByUserList;
    private Map<String, Activity> aux;

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

        downloadImageViewModel = new ViewModelProvider(getActivity()).get(DownloadImageViewModel.class);
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
                        aux = new HashMap<>();
                        for (Activity a : activitiesByUserList) {
                            Calendar currentTime = Calendar.getInstance();

                            String[] dateTime = a.getTime().split(" ");
                            String[] hours = dateTime[3].split(":");

                            Calendar beginTime = Calendar.getInstance();;
                            if (dateTime[4].equals("PM"))
                                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]) + 12 + Integer.valueOf(a.getDurationInMinutes())/60, Integer.valueOf(hours[1]) + Integer.valueOf(a.getDurationInMinutes())%60);
                            else
                                beginTime.set(Integer.valueOf(dateTime[2]), monthToIntegerShort(dateTime[0]), Integer.valueOf(dateTime[1].substring(0, dateTime[1].length()-1)), Integer.valueOf(hours[0]) +  Integer.valueOf(a.getDurationInMinutes())/60, Integer.valueOf(hours[1]) +  Integer.valueOf(a.getDurationInMinutes())%60);

                            long startMillis = beginTime.getTimeInMillis();
                            if(startMillis <= currentTime.getTimeInMillis()){
                                aux.put(a.getOwner() + "_" + a.getName(), a);
                                try {
                                    downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", a.getOwner() + "_" + a.getName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (aux.isEmpty())
                            relativeLayout.setVisibility(View.VISIBLE);
                        else {
                            List<Activity> activityList = new ArrayList<>(aux.values());

                            ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(getContext(), activityList, user);
                            enrolledActRecyclerView.setAdapter(adapter);
                            enrolledActRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    }

                }
            }
        });

        downloadImageViewModel.getImage().observe(getActivity(), image -> {
            if (image.getObjName().split("_").length == 2) {
                if (aux != null) {
                    String objName = image.getObjName();
                    Activity a = aux.get(objName);
                    if (a != null)
                        a.setImage(image);
                }
            }
        });

        return root;
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
