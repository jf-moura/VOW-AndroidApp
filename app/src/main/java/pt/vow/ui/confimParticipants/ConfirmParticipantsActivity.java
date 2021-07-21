package pt.vow.ui.confimParticipants;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
    private ConfirmParticipantsViewModel confirmParticipantsViewModel;
    private List<String> participantsList;
    private List<String> participantsConfirmedList;
    private RecyclerView confirmPartRecyclerView;
    private Button confirmBttn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_participants);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");
        participantsConfirmedList = new ArrayList<>();

        confirmPartRecyclerView = findViewById(R.id.confirm_part_recycler_view);
        confirmBttn = findViewById(R.id.confirmPartBttn);

        actParticipantsViewModel = new ViewModelProvider(this, new ActivityParticipantsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ActivityParticipantsViewModel.class);
        confirmParticipantsViewModel = new ViewModelProvider(this, new ConfirmParticipantsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ConfirmParticipantsViewModel.class);

        actParticipantsViewModel.getParticipants(user.getUsername(), user.getTokenID(), activity.getOwner(), activity.getId());
        actParticipantsViewModel.getParticipantsList().observe(this, participants -> {
            participantsList = participants;
            ConfirmPartRecyclerViewAdapter adapter = new ConfirmPartRecyclerViewAdapter(getApplicationContext(), participantsList, participantsConfirmedList);
            confirmPartRecyclerView.setAdapter(adapter);
            confirmPartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        });

        confirmBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmParticipantsViewModel.confirmParticipants(user.getUsername(), user.getTokenID(), activity.getId(), participantsConfirmedList);
            }
        });

        confirmParticipantsViewModel.getConfirmParticipantsResult().observe(this, confirmParticipantsResult -> {
            if (confirmParticipantsResult == null)
                return;
            if (confirmParticipantsResult.getError() != null)
                Toast.makeText(getApplicationContext(), confirmParticipantsResult.getError(), Toast.LENGTH_SHORT).show();
            if (confirmParticipantsResult.getSuccess() != null)
                Toast.makeText(getApplicationContext(), R.string.confirmed_participants, Toast.LENGTH_SHORT).show();
            finish();
        });

    }
}
