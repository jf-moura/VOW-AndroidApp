package pt.vow.ui.update;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.update.ChangeVisibilityRepository;

public class ChangeVisibilityViewModel extends ViewModel {
    private MutableLiveData<ChangeVisibilityResult> changeVisibilityResult = new MutableLiveData<>();
    private ChangeVisibilityRepository changeVisibilityRepository;

    private final Executor executor;

    ChangeVisibilityViewModel(ChangeVisibilityRepository changeVisibilityRepository, Executor executor) {
        this.changeVisibilityRepository = changeVisibilityRepository;
        this.executor = executor;
    }

    public LiveData<ChangeVisibilityResult> getVisibilityChangeResult() {
        return changeVisibilityResult;
    }

    public void changeVisibility(String username, String tokenID, String userToChange, boolean visibility) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<UpdatedUserView> result = changeVisibilityRepository.changeVisibility(username, tokenID, userToChange, visibility);
                if (result instanceof Result.Success) {
                    changeVisibilityResult.postValue(new ChangeVisibilityResult(new UpdatedUserView(visibility)));
                } else {
                    changeVisibilityResult.postValue(new ChangeVisibilityResult(R.string.update_failed));
                }
            }
        });
    }

}
