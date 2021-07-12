package pt.vow.ui.activityInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.rating.GetRatingDataSource;
import pt.vow.data.rating.GetRatingRepository;

public class GetRatingViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public GetRatingViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetRatingViewModel.class)) {
            return (T) new GetRatingViewModel(GetRatingRepository.getInstance(new GetRatingDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
