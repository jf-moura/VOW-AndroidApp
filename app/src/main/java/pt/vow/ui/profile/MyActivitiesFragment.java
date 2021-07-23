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
    private DownloadImageViewModel downloadImageViewModel;
    private LoggedInUserView user;

    private List<Activity> myActivitiesList;
    private Map<String, Activity> aux;

    private Observer<GetMyActivitiesResult> myActObs;

    private RelativeLayout relativeLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = ScrollviewActivitiesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");

        myActRecyclerView = root.findViewById(R.id.activities_recycler_view_profile);

        relativeLayout = root.findViewById(R.id.empty_state);

        downloadImageViewModel = new ViewModelProvider(getActivity()).get(DownloadImageViewModel.class);
        getMyActivitiesViewModel = new ViewModelProvider(getActivity()).get(GetMyActivitiesViewModel.class);

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
                    else if (myActivitiesList != null) {
                        aux = new HashMap<>();
                        for (Activity a : myActivitiesList) {
                            aux.put(a.getId(), a);
                            if (a.getImage() == null) {
                                try {
                                    downloadImageViewModel.downloadImage("vow-project-311114", "vow_profile_pictures", a.getId());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (aux.isEmpty())
                            relativeLayout.setVisibility(View.VISIBLE);
                        else {
                            List<Activity> activityList = new ArrayList<>(aux.values());

                            adapter = new ProfileRecyclerViewAdapter(getContext(), activityList, user);
                            myActRecyclerView.setAdapter(adapter);
                            myActRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        }
                    }

                }
            }
        });

        downloadImageViewModel.getImage().observe(getActivity(), image -> {
            if (aux != null) {
                String objName = image.getObjName();
                Activity a = aux.get(objName);
                if (a != null)
                    a.setImage(image);
            }
            myActRecyclerView.setAdapter(adapter);
        });

        return root;
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
