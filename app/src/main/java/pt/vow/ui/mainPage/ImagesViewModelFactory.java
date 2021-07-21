package pt.vow.ui.mainPage;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

public class ImagesViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public ImagesViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ImagesViewModel.class)) {
            return (T) new ImagesViewModel(executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}