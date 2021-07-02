package pt.vow.ui.enroll;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.cancelEnroll.CancelEnrollDataSource;
import pt.vow.data.cancelEnroll.CancelEnrollRepository;

public class CancelEnrollViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public CancelEnrollViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(CancelEnrollViewModel.class)) {
            return (T) new CancelEnrollViewModel(CancelEnrollRepository.getInstance(new CancelEnrollDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
