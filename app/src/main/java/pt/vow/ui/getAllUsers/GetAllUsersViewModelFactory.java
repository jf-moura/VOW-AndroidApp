package pt.vow.ui.getAllUsers;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;
import pt.vow.data.getAllUsers.GetAllUsersDataSource;
import pt.vow.data.getAllUsers.GetAllUsersRepository;

public class GetAllUsersViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public GetAllUsersViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetAllUsersViewModel.class)) {
            return (T) new GetAllUsersViewModel(GetAllUsersRepository.getInstance(new GetAllUsersDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
