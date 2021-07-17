package pt.vow.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getProfile.GetProfileRepository;
import pt.vow.data.model.UserInfo;
import pt.vow.ui.activityInfo.GetRatingView;


public class GetProfileViewModel extends ViewModel {
    private MutableLiveData<GetProfileResult> getProfileResult = new MutableLiveData<>();
    private MutableLiveData<UserInfoView> info = new MutableLiveData<>();
    private GetProfileRepository getProfileRepository;
    private final Executor executor;

    public GetProfileViewModel(GetProfileRepository getProfileRepository, Executor executor) {
        this.getProfileRepository = getProfileRepository;
        this.executor = executor;
    }

    public LiveData<GetProfileResult> getProfileResult() {
        return getProfileResult;
    }


    public void getProfile(String username, String tokenID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<UserInfoView> result = getProfileRepository.getProfile(username, tokenID);
                if (result instanceof Result.Success) {
                    UserInfoView data = ((Result.Success<UserInfoView>) result).getData();
                    info.postValue(data);
                    getProfileResult.postValue(new GetProfileResult(new UserInfoView(data.getUsername(), data.getTokenID(), data.getName(), data.getEmail(), data.getPhoneNumber(), data.getDateBirth(), data.getBio(), data.getWebsite())));
                } else {
                    getProfileResult.postValue(new GetProfileResult(R.string.get_profile_failed));
                }
            }
        });
    }

    public LiveData<UserInfoView> profile() {
        return info;
    }
}
