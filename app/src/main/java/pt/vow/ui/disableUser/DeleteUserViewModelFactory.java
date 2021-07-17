package pt.vow.ui.disableUser;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.disableUser.DeleteUserDataSource;
import pt.vow.data.disableUser.DeleteUserRepository;

public class DeleteUserViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public DeleteUserViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DeleteUserViewModel.class)) {
            return (T) new DeleteUserViewModel(DeleteUserRepository.getInstance(new DeleteUserDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
