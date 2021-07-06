package pt.vow.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getMyActivities.GetMyActivitiesRepository;
import pt.vow.data.model.Activity;

public class GetMyActivitiesViewModel extends ViewModel {
    private MutableLiveData<GetMyActivitiesResult> getActivitiesResult = new MutableLiveData<>();
    private MutableLiveData<List<Activity>> activities = new MutableLiveData<>();
    private GetMyActivitiesRepository activitiesInfoRepository;
    private final Executor executor;

    GetMyActivitiesViewModel(GetMyActivitiesRepository activitiesInfoRepository, Executor executor) {
        this.activitiesInfoRepository = activitiesInfoRepository;
        this.executor = executor;
    }

    public LiveData<GetMyActivitiesResult> getActivitiesResult() {
        return getActivitiesResult;
    }

    public void getActivities(String username, String tokenID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<MyActivitiesView> result = activitiesInfoRepository.getMyActivities(username, tokenID);
                if (result instanceof Result.Success) {
                    MyActivitiesView data = ((Result.Success<MyActivitiesView>) result).getData();
                    activities.postValue(data.getActivities());
                    getActivitiesResult.postValue(new GetMyActivitiesResult(new MyActivitiesView(data.activities)));
                } else {
                    getActivitiesResult.postValue(new GetMyActivitiesResult(R.string.get_activities_failed));
                }
            }
        });
    }

    public LiveData<List<Activity>> getActivitiesList() {
        return activities;
    }

}