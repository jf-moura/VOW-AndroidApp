package pt.vow.ui.newActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.registerActivity.NewActivityRepository;

public class NewActivityViewModel extends ViewModel {

    private MutableLiveData<pt.vow.ui.newActivity.NewActivityFormState> newActFormState = new MutableLiveData<>();
    private MutableLiveData<NewActivityResult> newActResult = new MutableLiveData<>();
    private NewActivityRepository newActivityRepository;
    private final Executor executor;

    NewActivityViewModel(NewActivityRepository newActivityRepository, Executor executor) {
        this.newActivityRepository = newActivityRepository;
        this.executor = executor;
    }

    LiveData<pt.vow.ui.newActivity.NewActivityFormState> getNewActFormState() {
        return newActFormState;
    }

    LiveData<NewActivityResult> getNewActResult() {
        return newActResult;
    }

    public void registerActivity(String username, String tokenID, String name, String address, String time, String participantNum, String durationInMinutes) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredActivity> result = newActivityRepository.registerActivity(username, tokenID, name, address, time, participantNum, durationInMinutes);
                if (result instanceof Result.Success) {
                    RegisteredActivity data = ((Result.Success<RegisteredActivity>) result).getData();
                    newActResult.postValue(new NewActivityResult(new RegisteredActivityView()));
                } else {
                    newActResult.postValue(new NewActivityResult(R.string.register_failed));
                }
            }
        });
    }

    public void newActivityDataChanged(String name, String address, String time, String participantNum, String durationInMinutes) {
        if (!isNameValid(name))
            newActFormState.setValue(new NewActivityFormState(R.string.invalid_name, null, null, null, null, null));
        else if (!isAddressValid(address))
            newActFormState.setValue(new NewActivityFormState(null, R.string.invalid_address, null, null, null, null));
        else if (!isTimeValid(time))
            newActFormState.setValue(new NewActivityFormState(null, null, R.string.invalid_time, null, null, null));
        else if (!isParticipantNumValid(participantNum))
            newActFormState.setValue(new NewActivityFormState(null, null, null, R.string.invalid_participant_num, null, null));
        else if (!isDurationInMinutesValid(durationInMinutes))
            newActFormState.setValue(new NewActivityFormState(null, null, null, null, R.string.invalid_duration, null));
        else
            newActFormState.setValue(new NewActivityFormState(true));
    }

    // A placeholder name validation check
    private boolean isNameValid(String name) {
        return name != null && !name.trim().isEmpty();
    }

    // A placeholder address validation check
    private boolean isAddressValid(String address) {
        return address != null && !address.trim().isEmpty();
    }

    // A placeholder time validation check
    private boolean isTimeValid(String time) {
        return time != null && !time.trim().isEmpty();
    }

    // A placeholder duration validation check
    private boolean isDurationInMinutesValid(String duration) {
        return duration != null && !duration.trim().isEmpty() && duration.compareTo("0") >= 1;
    }

    // A placeholder participant number validation check
    private boolean isParticipantNumValid(String participantNum) {
        if (participantNum != null)
            return  !participantNum.trim().isEmpty() && participantNum.compareTo("0") >= 1;
        return true;
    }

}