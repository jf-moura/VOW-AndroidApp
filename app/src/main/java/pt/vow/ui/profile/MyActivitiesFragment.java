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
import pt.vow.databinding.ScrollviewMyActivitiesBinding;
import pt.vow.ui.login.LoggedInUserView;

public class MyActivitiesFragment extends Fragment {
    private ScrollviewMyActivitiesBinding binding;

    private RecyclerView myActRecyclerView;

    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private LoggedInUserView user;

    private List<Activity> myActivitiesList;

    private Observer<GetMyActivitiesResult> myActObs;

    private RelativeLayout relativeLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ScrollviewMyActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        myActRecyclerView = root.findViewById(R.id.my_activities_recycler_view_profile);

        relativeLayout = root.findViewById(R.id.empty_state_mine);

        getMyActivitiesViewModel = new ViewModelProvider(requireActivity()).get(GetMyActivitiesViewModel.class);
        getMyActivitiesViewModel.getActivitiesResult().observeForever(myActObs = new Observer<GetMyActivitiesResult>() {
            @Override
            public void onChanged(@Nullable GetMyActivitiesResult getActivitiesResult) {
                if (getActivitiesResult == null) {
                    return;
                }
                if (getActivitiesResult.getError() != null) {
                    showGetActivitiesFailed(getActivitiesResult.getError());
                }
                if (getActivitiesResult.getSuccess() != null) {
                    myActivitiesList = getActivitiesResult.getSuccess().getActivities();
                    if (myActivitiesList.size() == 0) {
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                    if (myActivitiesList != null) {
                        ProfileRecyclerViewAdapter adapter = new ProfileRecyclerViewAdapter(getContext(), myActivitiesList, user);
                        myActRecyclerView.setAdapter(adapter);
                        myActRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }

                }
            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getMyActivitiesViewModel.getActivities(user.getUsername(), user.getTokenID());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (myActObs != null)
            getMyActivitiesViewModel.getActivitiesResult().removeObserver(myActObs);
    }

    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}
