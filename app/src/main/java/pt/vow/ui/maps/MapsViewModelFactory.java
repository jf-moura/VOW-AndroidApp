package pt.vow.ui.maps;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.getActivities.GetActivitiesDataSource;
import pt.vow.data.getActivities.GetActivitiesRepository;
import pt.vow.ui.feed.FeedViewModel;

public class MapsViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public MapsViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapsViewModel.class)) {
            return (T) new MapsViewModel(GetActivitiesRepository.getInstance(new GetActivitiesDataSource()), executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
