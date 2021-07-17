package pt.vow.ui.disableUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.disableUser.DeleteUserRepository;
import pt.vow.data.model.DeleteRegisteredUser;

public class DeleteUserViewModel extends ViewModel {
    private MutableLiveData<DeleteUserResult> deleteUserResult = new MutableLiveData<>();
    private DeleteUserRepository deleteUserRepository;

    private final Executor executor;

    DeleteUserViewModel(DeleteUserRepository deleteUserRepository, Executor executor) {
        this.deleteUserRepository = deleteUserRepository;
        this.executor = executor;
    }

    public LiveData<DeleteUserResult> getDeleteUserResult() {
        return deleteUserResult;
    }

    public void deleteUser(String username, String tokenID, String userToDelete) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<DeleteRegisteredUser> result = deleteUserRepository.deleteUser(username, tokenID, userToDelete);
                if (result instanceof Result.Success) {
                    DeleteRegisteredUser data = ((Result.Success<DeleteRegisteredUser>) result).getData();
                    deleteUserResult.postValue(new DeleteUserResult(new DeleteUserView(data.getDisplayName())));
                } else {
                    deleteUserResult.postValue(new DeleteUserResult(R.string.enroll_failed));
                }
            }
        });
    }
}
