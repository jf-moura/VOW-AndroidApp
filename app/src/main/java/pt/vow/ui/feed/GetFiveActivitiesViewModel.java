package pt.vow.ui.feed;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.comments.GetActCommentsRepository;
import pt.vow.data.getActivities.GetFiveActivitiesRepository;
import pt.vow.data.model.Commentary;
import pt.vow.ui.comments.CommentsRegisteredView;
import pt.vow.ui.comments.GetActCommentsResult;

public class GetFiveActivitiesViewModel extends ViewModel {
    private MutableLiveData<GetFiveActivitiesResult> getFiveActivitiesResult = new MutableLiveData<>();
    private MutableLiveData<FiveActivitiesView> activities = new MutableLiveData<>();
    private GetFiveActivitiesRepository getFiveActivitiesRepository;
    private final Executor executor;

    public GetFiveActivitiesViewModel(GetFiveActivitiesRepository getFiveActivitiesRepository, Executor executor) {
        this.getFiveActivitiesRepository = getFiveActivitiesRepository;
        this.executor = executor;
    }

    public LiveData<GetFiveActivitiesResult> getActivitiesResult() {
        return getFiveActivitiesResult;
    }


    public void getActivites(String username, String tokenID, int index) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<FiveActivitiesView> result = getFiveActivitiesRepository.getActivities(username, tokenID, index);
                if (result instanceof Result.Success) {
                    FiveActivitiesView data = ((Result.Success<FiveActivitiesView>) result).getData();
                    activities.postValue(data);
                    getFiveActivitiesResult.postValue(new GetFiveActivitiesResult(new FiveActivitiesView(data.getActivities())));
                } else {
                    getFiveActivitiesResult.postValue(new GetFiveActivitiesResult(R.string.get_activities_failed));
                }
            }
        });
    }

    public LiveData<FiveActivitiesView> getActivitiesList() {
        return activities;
    }

}
