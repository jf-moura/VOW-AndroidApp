package pt.vow.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.logout.LogoutRepository;
import pt.vow.data.model.LoggedOutUser;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<LogoutResult> logoutResult = new MutableLiveData<>();
    private LogoutRepository logoutRepository;

    private final Executor executor;

    LogoutViewModel(LogoutRepository logoutRepository, Executor executor) {
        this.logoutRepository = logoutRepository;
        this.executor = executor;
    }

    LiveData<LogoutResult> getLogoutResult() {
        return logoutResult;
    }

    public void logout(String username, String tokenID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<LoggedOutUser> result = logoutRepository.logout(username, tokenID);
                if (result instanceof Result.Success) {
                    LoggedOutUser data = ((Result.Success<LoggedOutUser>) result).getData();
                    logoutResult.postValue(new LogoutResult(new LoggedOutUserView(data.getDisplayName())));
                } else {
                    logoutResult.postValue(new LogoutResult(R.string.logout_failed));
                }
            }
        });
    }



}
