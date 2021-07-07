package pt.vow.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.ScrollviewEnrolledActivitiesBinding;
import pt.vow.ui.login.LoggedInUserView;

public class EnrolledActivitiesFragment extends Fragment {

    private ScrollviewEnrolledActivitiesBinding binding;

    private RecyclerView enrolledActRecyclerView;

    private GetActivitiesByUserViewModel getActivitiesByUserViewModel;
    private LoggedInUserView user;

    private List<Activity> activitiesByUserList;

    private Observer<GetActivitiesByUserResult> actByUserObs;

    private RelativeLayout relativeLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = ScrollviewEnrolledActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        enrolledActRecyclerView = root.findViewById(R.id.enrolled_activities_recycler_view_profile);

        relativeLayout = root.findViewById(R.id.empty_state_enrolled);

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
                        ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(getContext(), activitiesByUserList, user);
                        enrolledActRecyclerView.setAdapter(adapter);
                        enrolledActRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

}
