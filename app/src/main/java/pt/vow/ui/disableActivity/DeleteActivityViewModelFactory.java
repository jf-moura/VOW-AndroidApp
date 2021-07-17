package pt.vow.ui.disableActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.disableActivity.DeleteActivityDataSource;
import pt.vow.data.disableActivity.DeleteActivityRepository;

public class DeleteActivityViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public DeleteActivityViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DeleteActivityViewModel.class)) {
            return (T) new DeleteActivityViewModel(DeleteActivityRepository.getInstance(new DeleteActivityDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
