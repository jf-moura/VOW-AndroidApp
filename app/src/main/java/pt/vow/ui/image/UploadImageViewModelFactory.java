package pt.vow.ui.image;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

public class UploadImageViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public UploadImageViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UploadImageViewModel.class)) {
            return (T) new UploadImageViewModel(executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
