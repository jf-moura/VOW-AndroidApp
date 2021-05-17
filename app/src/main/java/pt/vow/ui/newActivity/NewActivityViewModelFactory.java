package pt.vow.ui.newActivity;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.registerActivity.NewActivityDataSource;
import pt.vow.data.registerActivity.NewActivityRepository;

public class NewActivityViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public NewActivityViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(NewActivityViewModel.class)) {
            return (T) new NewActivityViewModel(NewActivityRepository.getInstance(new NewActivityDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
