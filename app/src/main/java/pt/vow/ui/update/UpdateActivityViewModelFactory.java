package pt.vow.ui.update;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.update.UpdateDataSource;
import pt.vow.data.update.UpdateRepository;
import pt.vow.data.updateActivity.UpdateActivityDataSource;
import pt.vow.data.updateActivity.UpdateActivityRepository;

public class UpdateActivityViewModelFactory implements ViewModelProvider.Factory {

    private Executor executor;

    public UpdateActivityViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UpdateActivityViewModel.class)) {
            return (T) new UpdateActivityViewModel(UpdateActivityRepository.getInstance(new UpdateActivityDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
