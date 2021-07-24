package pt.vow.ui.restoreActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.restoreActivity.RestoreActivityRepository;
import pt.vow.ui.newActivity.RegisteredActivityView;

public class RestoreActivityViewModel extends ViewModel {
    private MutableLiveData<RestoreActivityResult> restoreActivityResult = new MutableLiveData<>();
    private RestoreActivityRepository restoreActivityRepository;
    private final Executor executor;

    RestoreActivityViewModel(RestoreActivityRepository restoreActivityRepository, Executor executor) {
        this.restoreActivityRepository = restoreActivityRepository;
        this.executor = executor;
    }

    public LiveData<RestoreActivityResult> getRestoreActivityResult() {
        return restoreActivityResult;
    }

    public void restoreActivity(String username, String tokenID, String activityOwner, String activityID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<Void> result = restoreActivityRepository.restoreActivity(username, tokenID, activityOwner, activityID);
                if (result instanceof Result.Success) {
                    restoreActivityResult.postValue(new RestoreActivityResult(new RegisteredActivityView(Long.getLong(activityID))));
                } else {
                    restoreActivityResult.postValue(new RestoreActivityResult(R.string.restore_activity_failed));
                }
            }
        });
    }

}
