package pt.vow.ui.disableActivity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.disableActivity.DeleteActivityRepository;
import pt.vow.data.model.DeleteCreatedActivity;

public class DeleteActivityViewModel extends ViewModel {
    private MutableLiveData<DeleteActivityResult> deleteActivityResult = new MutableLiveData<>();
    private DeleteActivityRepository deleteActivityRepository;

    private final Executor executor;

    DeleteActivityViewModel(DeleteActivityRepository deleteActivityRepository, Executor executor) {
        this.deleteActivityRepository = deleteActivityRepository;
        this.executor = executor;
    }

    public LiveData<DeleteActivityResult> getDeleteActivityResult() {
        return deleteActivityResult;
    }

    public void deleteActivity(String username, String tokenID, String activityOwner, String activityID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<DeleteCreatedActivity> result = deleteActivityRepository.deleteActivity(username, tokenID, activityOwner, activityID);
                if (result instanceof Result.Success) {
                    DeleteCreatedActivity data = ((Result.Success<DeleteCreatedActivity>) result).getData();
                    deleteActivityResult.postValue(new DeleteActivityResult(new DeleteActivityView(data.getDisplayName())));
                } else {
                    deleteActivityResult.postValue(new DeleteActivityResult(R.string.enroll_failed));
                }
            }
        });
    }
}
