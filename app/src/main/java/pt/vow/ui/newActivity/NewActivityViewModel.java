package pt.vow.ui.newActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.registerActivity.NewActivityRepository;

public class NewActivityViewModel extends ViewModel {
    private MutableLiveData<NewActivityFormState> newActFormState = new MutableLiveData<>();
    private MutableLiveData<NewActivityResult> newActResult = new MutableLiveData<>();
    private NewActivityRepository newActivityRepository;
    private final Executor executor;

    NewActivityViewModel(NewActivityRepository newActivityRepository, Executor executor) {
        this.newActivityRepository = newActivityRepository;
        this.executor = executor;
    }

    LiveData<NewActivityFormState> getNewActFormState() {
        return newActFormState;
    }

    LiveData<NewActivityResult> getNewActResult() {
        return newActResult;
    }

    public void registerActivity(String username, String tokenID, String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes, String description) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredActivity> result = newActivityRepository.registerActivity(username, tokenID, name, address, coordinates, time, type, participantNum, durationInMinutes, description);
                if (result instanceof Result.Success) {
                    newActResult.postValue(new NewActivityResult(new RegisteredActivityView(name)));
                } else {
                    newActResult.postValue(new NewActivityResult(R.string.register_failed));
                }
            }
        });
    }

    public void newActivityDataChanged(String name, String address, String time, String type, String participantNum, String durationInMinutes, String description) {
        if (!isNameValid(name))
            newActFormState.setValue(new NewActivityFormState(R.string.invalid_name, null));
        else if (!isAddressValid(address))
            newActFormState.setValue(new NewActivityFormState(null, null));
        else if (!isParticipantNumValid(participantNum))
            newActFormState.setValue(new NewActivityFormState(null, R.string.invalid_participant_num));
        else if (!isDurationInMinutesValid(durationInMinutes))
            newActFormState.setValue(new NewActivityFormState(null, null));
        else if (!isTimeValid(time))
            newActFormState.setValue(new NewActivityFormState(null, null));
        else if (!isTypeValid(type))
            newActFormState.setValue(new NewActivityFormState(null, null));
        else if (!isDescriptionValid(description))
            newActFormState.setValue(new NewActivityFormState(null, null));
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

    private int monthToIntegerShort(String month) {
        int result = 0;
        switch (month) {
            case "Jan":
                result = 0;
                break;
            case "Fev":
                result = 1;
                break;
            case "Mar":
                result = 2;
                break;
            case "Apr":
                result = 3;
                break;
            case "May":
                result = 4;
                break;
            case "Jun":
                result = 5;
                break;
            case "Jul":
                result = 6;
                break;
            case "Aug":
                result = 7;
                break;
            case "Sep":
                result = 8;
                break;
            case "Oct":
                result = 9;
                break;
            case "Nov":
                result = 10;
                break;
            case "Dec":
                result = 11;
                break;
        }
        return result;
    }

}