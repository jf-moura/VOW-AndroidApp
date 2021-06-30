package pt.vow.ui.getActivities;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getActivities.GetActivitiesRepository;
import pt.vow.data.model.Activity;

public class GetActivitiesViewModel extends ViewModel {

    private MutableLiveData<GetActivitiesResult> getActivitiesResult = new MutableLiveData<>();
    private MutableLiveData<List<Activity>> activities = new MutableLiveData<>();
    private GetActivitiesRepository activitiesInfoRepository;
    private final Executor executor;

    GetActivitiesViewModel(GetActivitiesRepository activitiesInfoRepository, Executor executor) {
        this.activitiesInfoRepository = activitiesInfoRepository;
        this.executor = executor;
    }

    LiveData<GetActivitiesResult> getActivitiesResult() {
        return getActivitiesResult;
    }


    public void getActivities(String username, String tokenID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<ActivitiesRegisteredView> result = activitiesInfoRepository.getActivities(username, tokenID);
                if (result instanceof Result.Success) {
                    ActivitiesRegisteredView data = ((Result.Success<ActivitiesRegisteredView>) result).getData();
                    activities.postValue(data.getActivities());
                    getActivitiesResult.postValue(new GetActivitiesResult(new ActivitiesRegisteredView(data.activities)));
                } else {
                    getActivitiesResult.postValue(new GetActivitiesResult(R.string.get_activities_failed));
                }
            }
        });
    }

    public LiveData<List<Activity>> getActivitiesList() {
        return activities;
    }
}