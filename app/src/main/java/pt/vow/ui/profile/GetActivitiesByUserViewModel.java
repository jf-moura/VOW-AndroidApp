package pt.vow.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getActivitiesByUser.GetActivitiesByUserRepository;
import pt.vow.data.model.Activity;

public class GetActivitiesByUserViewModel extends ViewModel {
    private MutableLiveData<GetActivitiesByUserResult> getActivitiesResult = new MutableLiveData<>();
    private MutableLiveData<List<Activity>> activities = new MutableLiveData<>();
    private GetActivitiesByUserRepository activitiesInfoRepository;
    private final Executor executor;

    GetActivitiesByUserViewModel(GetActivitiesByUserRepository activitiesInfoRepository, Executor executor) {
        this.activitiesInfoRepository = activitiesInfoRepository;
        this.executor = executor;
    }

    public LiveData<GetActivitiesByUserResult> getActivitiesResult() {
        return getActivitiesResult;
    }

    public void getActivities(String username, String tokenID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<ActivitiesByUserRegisteredView> result = activitiesInfoRepository.getActivitiesByUser(username, tokenID);
                if (result instanceof Result.Success) {
                    ActivitiesByUserRegisteredView data = ((Result.Success<ActivitiesByUserRegisteredView>) result).getData();
                    activities.postValue(data.getActivities());
                    getActivitiesResult.postValue(new GetActivitiesByUserResult(new ActivitiesByUserRegisteredView(data.activities)));
                } else {
                    getActivitiesResult.postValue(new GetActivitiesByUserResult(R.string.get_activities_failed));
                }
            }
        });
    }

    public LiveData<List<Activity>> getActivitiesList() {
        return activities;
    }
}
