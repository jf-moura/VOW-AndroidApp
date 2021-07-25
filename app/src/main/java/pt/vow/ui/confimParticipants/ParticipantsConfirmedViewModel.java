package pt.vow.ui.confimParticipants;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.activityParticipants.ActivityParticipantsRepository;
import pt.vow.ui.activityInfo.ActivityParticipantsResult;
import pt.vow.ui.activityInfo.ActivityParticipantsView;

public class ParticipantsConfirmedViewModel extends ViewModel {
    private MutableLiveData<ActivityParticipantsResult> activityParticipantsResult = new MutableLiveData<>();
    private MutableLiveData<List<String>> participants = new MutableLiveData<>();
    private ActivityParticipantsRepository participantsRepository;
    private final Executor executor;

    public ParticipantsConfirmedViewModel(ActivityParticipantsRepository participantsRepository, Executor executor) {
        this.participantsRepository = participantsRepository;
        this.executor = executor;
    }

    public LiveData<ActivityParticipantsResult> getParticipantsResult() {
        return activityParticipantsResult;
    }

    public void getParticipants(String username, String tokenID, String owner, String activityId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<ActivityParticipantsView> result = participantsRepository.getParticipants(username, tokenID, true, owner, activityId);
                if (result instanceof Result.Success) {
                    ActivityParticipantsView data = ((Result.Success<ActivityParticipantsView>) result).getData();
                    participants.postValue(data.getParticipants());
                    activityParticipantsResult.postValue(new ActivityParticipantsResult(new ActivityParticipantsView(data.getActivityID(), data.getParticipants())));
                } else {
                    activityParticipantsResult.postValue(new ActivityParticipantsResult(R.string.act_participants_failed));
                }
            }
        });
    }

    public LiveData<List<String>> getParticipantsList() {
        return participants;
    }
}
