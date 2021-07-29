package pt.vow.ui.feed;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.getActivities.GetFiveActivitiesDataSource;
import pt.vow.data.getActivities.GetFiveActivitiesRepository;

public class GetFiveActivitiesViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public GetFiveActivitiesViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetFiveActivitiesViewModel.class)) {
            return (T) new GetFiveActivitiesViewModel(GetFiveActivitiesRepository.getInstance(new GetFiveActivitiesDataSource()), executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
