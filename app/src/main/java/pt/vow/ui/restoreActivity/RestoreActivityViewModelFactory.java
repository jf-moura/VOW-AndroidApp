package pt.vow.ui.restoreActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.restoreActivity.RestoreActivityDataSource;
import pt.vow.data.restoreActivity.RestoreActivityRepository;

public class RestoreActivityViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public RestoreActivityViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RestoreActivityViewModel.class)) {
            return (T) new RestoreActivityViewModel(RestoreActivityRepository.getInstance(new RestoreActivityDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
