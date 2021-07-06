package pt.vow.ui.route;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.route.NewRouteRepository;
import pt.vow.ui.newActivity.RegisteredActivityView;

public class NewRouteViewModel extends ViewModel {
    private MutableLiveData<NewRouteFormState> newRouteFormState = new MutableLiveData<>();
    private MutableLiveData<NewRouteResult> newRouteResult = new MutableLiveData<>();
    private NewRouteRepository newRouteRepository;
    private final Executor executor;

    NewRouteViewModel(NewRouteRepository newRouteRepository, Executor executor) {
        this.newRouteRepository = newRouteRepository;
        this.executor = executor;
    }

    LiveData<NewRouteFormState> getNewRouteFormState() {
        return newRouteFormState;
    }

    LiveData<NewRouteResult> getNewRouteResult() {
        return newRouteResult;
    }

    public void registerRoute(String username, String tokenID, String name, String address, String time, String type, String participantNum, String durationInMinutes,  String[] coordinateArray) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<RegisteredActivity> result = newRouteRepository.registerRoute(username, tokenID, name, address, time,type, participantNum, durationInMinutes, coordinateArray);
                if (result instanceof Result.Success) {
                    newRouteResult.postValue(new NewRouteResult(new RegisteredActivityView()));
                } else {
                    newRouteResult.postValue(new NewRouteResult(R.string.register_failed));
                }
            }
        });
    }

    public void newRouteDataChanged(String name, String time, String type, String participantNum, String durationInMinutes) {
        if (!isNameValid(name))
            newRouteFormState.setValue(new NewRouteFormState(R.string.invalid_name, null, null, null, null, null, null));
        else if (!isTimeValid(time))
            newRouteFormState.setValue(new NewRouteFormState(null, null, R.string.invalid_time, null, null, null, null));
        else if (!isParticipantNumValid(participantNum))
            newRouteFormState.setValue(new NewRouteFormState(null, null, null, null, R.string.invalid_participant_num, null, null));
        else if (!isDurationInMinutesValid(durationInMinutes))
            newRouteFormState.setValue(new NewRouteFormState(null, null, null, null, null, R.string.invalid_duration, null));
        else if (!isTypeValid(type))
            newRouteFormState.setValue(new NewRouteFormState(null, null, null, null,  R.string.invalid_type,null, null));
        else
            newRouteFormState.setValue(new NewRouteFormState(true));
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
            return  !participantNum.trim().isEmpty() && participantNum.compareTo("0") >= 1;
        return true;
    }
    // A placeholder type validation check
    private boolean isTypeValid(String type) {
        return type != null;
    }

}
