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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private ConfirmParticipantsViewModel confirmParticipantsViewModel;
    private Map<String, Integer> participantsList; // 0 means participant wasn't confirmed yet, 1 otherwise
    private List<String> allParticipants;
    private List<String> participantsConfirmedList;
    private List<String> alreadyConfirmed;
    private RecyclerView confirmPartRecyclerView;
    private ConfirmPartRecyclerViewAdapter adapter;
    private Button confirmBttn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_participants);

        user = (LoggedInUserView) getIntent().getSerializableExtra("UserLogged");
        activity = (Activity) getIntent().getSerializableExtra("Activity");
        allParticipants = (List<String>) getIntent().getSerializableExtra("Participants");
        alreadyConfirmed = (List<String>) getIntent().getSerializableExtra("ConfirmedParticipants");
        participantsConfirmedList = new ArrayList<>();
        participantsList = new HashMap<>();

        confirmPartRecyclerView = findViewById(R.id.confirm_part_recycler_view);
        confirmBttn = findViewById(R.id.confirmPartBttn);

        confirmParticipantsViewModel = new ViewModelProvider(this, new ConfirmParticipantsViewModelFactory(((VOW) getApplication()).getExecutorService()))
                .get(ConfirmParticipantsViewModel.class);


        for (String part : allParticipants) {
            for (String confirmed : alreadyConfirmed)
                if (part.equals(confirmed))
                    participantsList.put(part, 1);
            if (participantsList.get(part) == null)
                participantsList.put(part, 0);
        }

        List<String> participantL = new ArrayList<>(participantsList.keySet());
        List<Integer> participantConf = new ArrayList<>(participantsList.values());

        adapter = new ConfirmPartRecyclerViewAdapter(getApplicationContext(), participantL, participantConf, participantsConfirmedList);
        confirmPartRecyclerView.setAdapter(adapter);
        confirmPartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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
