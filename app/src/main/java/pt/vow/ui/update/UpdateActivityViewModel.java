package pt.vow.ui.update;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.registerActivity.NewActivityRepository;
import pt.vow.data.updateActivity.UpdateActivityRepository;
import pt.vow.ui.newActivity.NewActivityFormState;
import pt.vow.ui.newActivity.NewActivityResult;
import pt.vow.ui.newActivity.RegisteredActivityView;

public class UpdateActivityViewModel extends ViewModel {
    private MutableLiveData<UpdateActivityFormState> updateActFormState = new MutableLiveData<>();
    private MutableLiveData<UpdateActivityResult> updateActResult = new MutableLiveData<>();
    private UpdateActivityRepository updateActivityRepository;
    private final Executor executor;

    UpdateActivityViewModel(UpdateActivityRepository updateActivityRepository, Executor executor) {
        this.updateActivityRepository = updateActivityRepository;
        this.executor = executor;
    }

    public LiveData<UpdateActivityFormState> getUpdateActFormState() {
        return updateActFormState;
    }

    public LiveData<UpdateActivityResult> getUpdateActResult() {
        return updateActResult;
    }

    public void updateActivity(String username, String tokenID, String activityOwner, Long activityID, String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes, String coordinateArray, Boolean append, String role, String description) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredActivity> result = updateActivityRepository.updateActivity(username, tokenID, activityOwner, activityID, name, address, coordinates, time, type, participantNum, durationInMinutes, coordinateArray, append, role, description);
                if (result instanceof Result.Success) {
                    //TODO: alterar
                    updateActResult.postValue(new UpdateActivityResult(new RegisteredActivity(name)));
                } else {
                    updateActResult.postValue(new UpdateActivityResult(R.string.update_failed));
                }
            }
        });
    }

    public void updateActivityDataChanged(String name, String address, String time, String type, String participantNum, String durationInMinutes, String description) {
        if (!isNameValid(name))
            updateActFormState.setValue(new UpdateActivityFormState(R.string.invalid_name, null));
        else if (!isAddressValid(address))
            updateActFormState.setValue(new UpdateActivityFormState(null, null));
        else if (!isParticipantNumValid(participantNum))
            updateActFormState.setValue(new UpdateActivityFormState(null, R.string.invalid_participant_num));
        else if (!isDurationInMinutesValid(durationInMinutes))
            updateActFormState.setValue(new UpdateActivityFormState(null, null));
        else if (!isTimeValid(time))
            updateActFormState.setValue(new UpdateActivityFormState(null, null));
        else if (!isTypeValid(type))
            updateActFormState.setValue(new UpdateActivityFormState(null, null));
        else if (!isDescriptionValid(description))
            updateActFormState.setValue(new UpdateActivityFormState(null, null));
        else
            updateActFormState.setValue(new UpdateActivityFormState(true));
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
        return duration != null;
    }

    // A placeholder participant number validation check
    private boolean isParticipantNumValid(String participantNum) {
        if (participantNum != null)
            return !participantNum.trim().isEmpty() && participantNum.compareTo("0") >= 1;
        return true;
    }

    // A placeholder type validation check
    private boolean isTypeValid(String type) {
        return type != null && !type.isEmpty();
    }

    private boolean isDescriptionValid(String description) {
        return !description.isEmpty();
    }

}