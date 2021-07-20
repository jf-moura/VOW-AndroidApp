package pt.vow.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getProfile.GetProfileRepository;


public class GetProfileViewModel extends ViewModel {
    private MutableLiveData<GetProfileResult> getProfileResult = new MutableLiveData<>();
    private MutableLiveData<ProfileInfoView> info = new MutableLiveData<>();
    private GetProfileRepository getProfileRepository;
    private final Executor executor;

    public GetProfileViewModel(GetProfileRepository getProfileRepository, Executor executor) {
        this.getProfileRepository = getProfileRepository;
        this.executor = executor;
    }

    public LiveData<GetProfileResult> getProfileResult() {
        return getProfileResult;
    }


    public void getProfile(String userToGet, String username, String tokenID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<ProfileInfoView> result = getProfileRepository.getProfile(userToGet, username, tokenID);
                if (result instanceof Result.Success) {
                    ProfileInfoView data = ((Result.Success<ProfileInfoView>) result).getData();
                    info.postValue(data);
                    getProfileResult.postValue(new GetProfileResult(new ProfileInfoView(data.getUsername(), data.getTokenID(), data.getName(), data.getEmail(), data.getPhoneNumber(), data.getDateBirth(), data.getBio(), data.getWebsite(), data.getVisibility(), data.getStatus())));
                } else {
                    getProfileResult.postValue(new GetProfileResult(R.string.get_profile_failed));
                }
            }
        });
    }

    public LiveData<ProfileInfoView> profile() {
        return info;
    }
}
