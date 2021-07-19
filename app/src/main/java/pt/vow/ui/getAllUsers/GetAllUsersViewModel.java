package pt.vow.ui.getAllUsers;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.getActivities.GetActivitiesRepository;
import pt.vow.data.getAllUsers.GetAllUsersRepository;
import pt.vow.data.model.Activity;
import pt.vow.data.model.UserInfo;
import pt.vow.data.model.UsersRegisteredView;
import pt.vow.ui.feed.ActivitiesRegisteredView;
import pt.vow.ui.feed.GetActivitiesResult;

public class GetAllUsersViewModel extends ViewModel {
    private MutableLiveData<GetAllUsersResult> getAllUsersResult = new MutableLiveData<>();
    private MutableLiveData<List<UserInfo>> users = new MutableLiveData<>();
    private GetAllUsersRepository getAllUsersRepository;
    private final Executor executor;

    public GetAllUsersViewModel(GetAllUsersRepository getAllUsersRepository, Executor executor) {
        this.getAllUsersRepository = getAllUsersRepository;
        this.executor = executor;
    }

    public LiveData<GetAllUsersResult> getAllUsersResult() {
        return getAllUsersResult;
    }


    public void getAllUsers(String username, String tokenID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<UsersRegisteredView> result = getAllUsersRepository.getUsers(username, tokenID);
                if (result instanceof Result.Success) {
                    UsersRegisteredView data = ((Result.Success<UsersRegisteredView>) result).getData();
                    users.postValue(data.getUsers());
                    getAllUsersResult.postValue(new GetAllUsersResult(new UsersRegisteredView(data.users)));
                } else {
                    getAllUsersResult.postValue(new GetAllUsersResult(R.string.get_activities_failed));
                }
            }
        });
    }

    public LiveData<List<UserInfo>> getAllUsersList() {
        return users;
    }
}
