package pt.vow.ui.enroll;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.cancelEnroll.CancelEnrollRepository;
import pt.vow.data.model.CancelEnrolledActivity;

public class CancelEnrollViewModel extends ViewModel {
    private MutableLiveData<CancelEnrollResult> cancelEnrollResult = new MutableLiveData<>();
    private CancelEnrollRepository cancelEnrollRepository;

    private final Executor executor;

    CancelEnrollViewModel(CancelEnrollRepository cancelEnrollRepository, Executor executor) {
        this.cancelEnrollRepository = cancelEnrollRepository;
        this.executor = executor;
    }

    LiveData<CancelEnrollResult> getCancelEnrollResult() {
        return cancelEnrollResult;
    }

    public void cancelEnrollInActivity(String username, String tokenID, String activityOwner, String activityID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<CancelEnrolledActivity> result = cancelEnrollRepository.cancelEnrollInActivity(username, tokenID, activityOwner, activityID);
                if (result instanceof Result.Success) {
                    CancelEnrolledActivity data = ((Result.Success<CancelEnrolledActivity>) result).getData();
                    cancelEnrollResult.postValue(new CancelEnrollResult(new CancelEnrolledActivityView(data.getDisplayName())));
                } else {
                    cancelEnrollResult.postValue(new CancelEnrollResult(R.string.enroll_failed));
                }
            }
        });
    }
}
