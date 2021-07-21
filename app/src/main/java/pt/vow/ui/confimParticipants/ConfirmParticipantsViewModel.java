package pt.vow.ui.confimParticipants;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.activityParticipants.ConfirmParticipantsRepository;
import pt.vow.data.rating.GetRatingRepository;
import pt.vow.ui.activityInfo.GetRatingResult;
import pt.vow.ui.activityInfo.GetRatingView;

public class ConfirmParticipantsViewModel extends ViewModel {
    private MutableLiveData<ConfirmParticipantsResult> confirmParticipantsResult = new MutableLiveData<>();
    private MutableLiveData<ParticipantsConfirmed> participantsConfirmed = new MutableLiveData<>();
    private ConfirmParticipantsRepository confirmParticipantsRepository;
    private final Executor executor;

    public ConfirmParticipantsViewModel(ConfirmParticipantsRepository confirmParticipantsRepository, Executor executor) {
        this.confirmParticipantsRepository = confirmParticipantsRepository;
        this.executor = executor;
    }

    public LiveData<ConfirmParticipantsResult> getConfirmParticipantsResult() {
        return confirmParticipantsResult;
    }

    public void confirmParticipants(String username, String tokenID, String activityID, List<String> participants) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<ParticipantsConfirmed> result = confirmParticipantsRepository.confirmParticipants(username, tokenID, activityID, participants);
                if (result instanceof Result.Success) {
                    ParticipantsConfirmed data = ((Result.Success<ParticipantsConfirmed>) result).getData();
                    participantsConfirmed.postValue(data);
                    confirmParticipantsResult.postValue(new ConfirmParticipantsResult(new ParticipantsConfirmed(data.getActivityID(), data.getParticipants())));
                } else {
                    confirmParticipantsResult.postValue(new ConfirmParticipantsResult(R.string.confirm_participants_failed));
                }
            }
        });
    }

    public LiveData<ParticipantsConfirmed> participantsConfirmed() {
        return participantsConfirmed;
    }

}
