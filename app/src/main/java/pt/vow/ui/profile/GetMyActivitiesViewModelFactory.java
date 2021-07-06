package pt.vow.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.getMyActivities.GetMyActivitiesDataSource;
import pt.vow.data.getMyActivities.GetMyActivitiesRepository;

public class GetMyActivitiesViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public GetMyActivitiesViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetMyActivitiesViewModel.class)) {
            return (T) new GetMyActivitiesViewModel(GetMyActivitiesRepository.getInstance(new GetMyActivitiesDataSource()), executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}