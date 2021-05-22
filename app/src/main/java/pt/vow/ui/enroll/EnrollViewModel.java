package pt.vow.ui.enroll;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.enrollActivity.EnrollRepository;
import pt.vow.data.model.EnrolledActivity;


public class EnrollViewModel {

    private MutableLiveData<EnrollResult> enrollResult = new MutableLiveData<>();
    private EnrollRepository enrollRepository;

    private final Executor executor;

    EnrollViewModel(EnrollRepository enrollRepository, Executor executor) {
        this.enrollRepository =enrollRepository;
        this.executor = executor;
    }

    LiveData<EnrollResult> getRegisterResult() {
        return enrollResult;
    }

    public void enrollInActivity(String username, String tokenID, String activityOwner, String activityID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<EnrolledActivity> result = enrollRepository.enrollInActivity(username, tokenID, activityOwner, activityID);
                if (result instanceof Result.Success) {
                    EnrolledActivity data = ((Result.Success<EnrolledActivity>) result).getData();
                    enrollResult.postValue(new EnrollResult(new EnrolledActivityView(data.getDisplayName())));
                } else {
                    enrollResult.postValue(new EnrollResult(R.string.enroll_failed));
                }
            }
        });
    }


}
