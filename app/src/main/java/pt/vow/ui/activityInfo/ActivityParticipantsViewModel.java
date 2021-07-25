package pt.vow.ui.activityInfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.activityParticipants.ActivityParticipantsRepository;


public class ActivityParticipantsViewModel extends ViewModel {
    private MutableLiveData<ActivityParticipantsResult> activityParticipantsResult = new MutableLiveData<>();
    private MutableLiveData<ActivityParticipantsView> participants = new MutableLiveData<>();
    private ActivityParticipantsRepository participantsRepository;
    private final Executor executor;

    public ActivityParticipantsViewModel(ActivityParticipantsRepository participantsRepository, Executor executor) {
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
                Result<ActivityParticipantsView> result = participantsRepository.getParticipants(username, tokenID, false, owner, activityId);
                if (result instanceof Result.Success) {
                    ActivityParticipantsView data = ((Result.Success<ActivityParticipantsView>) result).getData();
                    participants.postValue(data);
                    activityParticipantsResult.postValue(new ActivityParticipantsResult(new ActivityParticipantsView(data.getActivityID(), data.participants)));
                } else {
                    activityParticipantsResult.postValue(new ActivityParticipantsResult(R.string.act_participants_failed));
                }
            }
        });
    }

    public LiveData<ActivityParticipantsView> getParticipantsList() {
        return participants;
    }
}
