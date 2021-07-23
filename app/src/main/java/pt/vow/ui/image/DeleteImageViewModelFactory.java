package pt.vow.ui.image;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

public class DeleteImageViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public DeleteImageViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DeleteImageViewModel.class)) {
            return (T) new DeleteImageViewModel(executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
