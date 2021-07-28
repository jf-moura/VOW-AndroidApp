package pt.vow.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getNearbyActivities.GetNearbyActRepository;
import pt.vow.data.model.Activity;
import pt.vow.data.model.NearbyActivitiesView;

public class GetNearbyActivitiesViewModel extends ViewModel {
    private MutableLiveData<GetNearbyActivitiesResult> getNearbyActivitiesResult = new MutableLiveData<>();
    private MutableLiveData<List<Activity>> activities = new MutableLiveData<>();
    private GetNearbyActRepository getNearbyActRepository;
    private final Executor executor;

    public GetNearbyActivitiesViewModel(GetNearbyActRepository getNearbyActRepository, Executor executor) {
        this.getNearbyActRepository = getNearbyActRepository;
        this.executor = executor;
    }

    public LiveData<GetNearbyActivitiesResult> getNearbyActResult() {
        return getNearbyActivitiesResult;
    }


    public void getNearbyActivities(String username, String tokenID, String p1Long, String p1Lat, String p2Long, String p2Lat) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<NearbyActivitiesView> result = getNearbyActRepository.getNearbyActivities(username, tokenID, p1Long, p1Lat, p2Long, p2Lat);
                if (result instanceof Result.Success) {
                    NearbyActivitiesView data = ((Result.Success<NearbyActivitiesView>) result).getData();
                    activities.postValue(data.getActivities());
                    getNearbyActivitiesResult.postValue(new GetNearbyActivitiesResult(new NearbyActivitiesView(data.activities)));
                } else {
                    getNearbyActivitiesResult.postValue(new GetNearbyActivitiesResult(R.string.get_activities_failed));
                }
            }
        });
    }

    public LiveData<List<Activity>> getNearbyActivitiesList() {
        return activities;
    }
}
