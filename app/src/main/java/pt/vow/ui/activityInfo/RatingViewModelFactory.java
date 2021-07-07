package pt.vow.ui.activityInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.rating.RatingDataSource;
import pt.vow.data.rating.RatingRepository;

public class RatingViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public RatingViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RatingViewModel.class)) {
            return (T) new RatingViewModel(RatingRepository.getInstance(new RatingDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
