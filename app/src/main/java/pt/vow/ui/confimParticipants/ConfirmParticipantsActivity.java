package pt.vow.ui.confimParticipants;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pt.vow.R;
import pt.vow.data.model.Activity;
import pt.vow.ui.VOW;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModel;
import pt.vow.ui.activityInfo.ActivityParticipantsViewModelFactory;
import pt.vow.ui.comments.CommentsRecyclerViewAdapter;
import pt.vow.ui.login.LoggedInUserView;

public class ConfirmParticipantsActivity extends AppCompatActivity {
    private LoggedInUserView user;
    private Activity activity;
    private ActivityParticipantsViewModel actParticipantsViewModel;
    private List<String> participantsList;
    private RecyclerView confirmPartRecyclerView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_participants);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");

        confirmPartRecyclerView = findViewById(R.id.confirm_part_recycler_view);

        actParticipantsViewModel = new ViewModelProvider(this, new ActivityParticipantsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ActivityParticipantsViewModel.class);

        actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
        actParticipantsViewModel.getParticipantsList().observe(this, participants -> {
            participantsList = participants;
            ConfirmPartRecyclerViewAdapter adapter = new ConfirmPartRecyclerViewAdapter(getApplicationContext(), participantsList);
            confirmPartRecyclerView.setAdapter(adapter);
            confirmPartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

    }
}
