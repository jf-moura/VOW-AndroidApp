package pt.vow.ui.update;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.update.ChangeVisibilityDataSource;
import pt.vow.data.update.ChangeVisibilityRepository;

public class ChangeVisibilityViewModelFactory implements ViewModelProvider.Factory {

    private Executor executor;

    public ChangeVisibilityViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ChangeVisibilityViewModel.class)) {
            return (T) new ChangeVisibilityViewModel(ChangeVisibilityRepository.getInstance(new ChangeVisibilityDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
