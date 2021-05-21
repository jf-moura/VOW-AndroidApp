package pt.vow.ui.maps;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getActivities.GetActivitiesRepository;

public class GetActivitiesViewModel extends ViewModel {

    private MutableLiveData<GetActivitiesResult> getActivitiesResult = new MutableLiveData<>();
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
                    getActivitiesResult.postValue(new GetActivitiesResult(new ActivitiesRegisteredView(data.activities)));
                } else {
                    getActivitiesResult.postValue(new GetActivitiesResult(R.string.get_activities_failed));
                }
            }
        });
    }

}