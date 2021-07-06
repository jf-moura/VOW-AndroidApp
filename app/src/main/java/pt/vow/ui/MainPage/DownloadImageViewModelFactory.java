package pt.vow.ui.MainPage;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

public class DownloadImageViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public DownloadImageViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DownloadImageViewModel.class)) {
            return (T) new DownloadImageViewModel(executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
