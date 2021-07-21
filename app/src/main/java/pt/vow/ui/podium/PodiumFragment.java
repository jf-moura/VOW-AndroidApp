package pt.vow.ui.podium;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.data.model.UserInfo;
import pt.vow.databinding.FragmentPodiumBinding;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.GetRatingViewModel;
import pt.vow.ui.activityInfo.GetRatingViewModelFactory;
import pt.vow.ui.feed.GetActivitiesViewModel;
import pt.vow.ui.feed.RecyclerViewAdapter;
import pt.vow.ui.getAllUsers.GetAllUsersViewModel;
import pt.vow.ui.getAllUsers.GetAllUsersViewModelFactory;
import pt.vow.ui.login.LoggedInUserView;

public class PodiumFragment extends Fragment {

    private PodiumViewModel podiumViewModel;
    private FragmentPodiumBinding binding;
    private RecyclerView recyclerView;
    private LoggedInUserView user;
    private GetAllUsersViewModel getAllUsersViewModel;
    private List<UserInfo> usersList;
    private ImageView imageViewInfo;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        podiumViewModel =
                new ViewModelProvider(this).get(PodiumViewModel.class);

        binding = FragmentPodiumBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        user = (LoggedInUserView) getActivity().getIntent().getSerializableExtra("UserLogged");
        getAllUsersViewModel = new ViewModelProvider(this, new GetAllUsersViewModelFactory(((VOW) getActivity().getApplication()).getExecutorService()))
                .get(GetAllUsersViewModel.class);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);

        recyclerView = root.findViewById(R.id.activities_recycler_view);
        imageViewInfo = root.findViewById(R.id.imageViewInfo);

        getAllUsersViewModel.getAllUsers(user.getUsername(), user.getTokenID());

        getAllUsersViewModel.getAllUsersList().observe(getActivity(), users -> {
            usersList = users;
            PodiumRecyclerViewAdapter adapter = new PodiumRecyclerViewAdapter(getContext(), usersList, user);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        });

        imageViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), R.string.podium_info, Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
