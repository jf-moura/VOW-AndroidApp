package pt.vow.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.databinding.ScrollviewActivitiesBinding;
import pt.vow.ui.login.LoggedInUserView;
import pt.vow.ui.image.DownloadImageViewModel;

public class MyActivitiesFragment extends Fragment {
    private ScrollviewActivitiesBinding binding;

    private RecyclerView myActRecyclerView;
    private ProfileRecyclerViewAdapter adapter;

    private GetMyActivitiesViewModel getMyActivitiesViewModel;
    private LoggedInUserView user;

    private List<Activity> myActivitiesList;

    private RelativeLayout relativeLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ScrollviewActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        myActRecyclerView = root.findViewById(R.id.activities_recycler_view_profile);

        relativeLayout = root.findViewById(R.id.empty_state);

        getMyActivitiesViewModel = new ViewModelProvider(getActivity()).get(GetMyActivitiesViewModel.class);

        getMyActivitiesViewModel.getActivitiesResult().observe(getActivity(), new Observer<GetMyActivitiesResult>() {
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
                        myActRecyclerView.setAdapter(null);
                    } else if (myActivitiesList != null) {
                        List<Activity> aux = new ArrayList<>();
                        for (Activity a : myActivitiesList) {
                            aux.add(a);
                        }
                        if (aux.isEmpty()) {
                            relativeLayout.setVisibility(View.VISIBLE);
                            myActRecyclerView.setAdapter(null);
                        } else {
                            relativeLayout.setVisibility(View.GONE);

                            adapter = new ProfileRecyclerViewAdapter(getContext(), aux, user);
                            myActRecyclerView.setAdapter(adapter);
                            myActRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    }

                }
            }
        });

        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        getMyActivitiesViewModel.getActivities(user.getUsername(), user.getUsername(), user.getTokenID());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void showGetActivitiesFailed(@StringRes Integer errorString) {
        Toast.makeText(getActivity().getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


}
