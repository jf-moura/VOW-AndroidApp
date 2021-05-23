package pt.vow.ui.feed;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.getActivities.GetActivitiesDataSource;
import pt.vow.data.getActivities.GetActivitiesRepository;

public class FeedViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public FeedViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FeedViewModel.class)) {
            return (T) new FeedViewModel(GetActivitiesRepository.getInstance(new GetActivitiesDataSource()), executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}