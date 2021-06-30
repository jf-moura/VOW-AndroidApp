package pt.vow.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.getActivitiesByUser.GetActivitiesByUserDataSource;
import pt.vow.data.getActivitiesByUser.GetActivitiesByUserRepository;

public class GetActivitiesByUserViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public GetActivitiesByUserViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetActivitiesByUserViewModel.class)) {
            return (T) new GetActivitiesByUserViewModel(GetActivitiesByUserRepository.getInstance(new GetActivitiesByUserDataSource()), executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
