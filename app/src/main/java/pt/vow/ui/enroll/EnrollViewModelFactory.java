package pt.vow.ui.enroll;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.enrollActivity.EnrollDataSource;
import pt.vow.data.enrollActivity.EnrollRepository;


public class EnrollViewModelFactory implements ViewModelProvider.Factory{
    private Executor executor;

    public EnrollViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EnrollViewModel.class)) {
            return (T) new EnrollViewModel(EnrollRepository.getInstance(new EnrollDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
