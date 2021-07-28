package pt.vow.ui.maps;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.getNearbyActivities.GetNearbyActDataSource;
import pt.vow.data.getNearbyActivities.GetNearbyActRepository;

public class GetNearbyActivitiesViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public GetNearbyActivitiesViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetNearbyActivitiesViewModel.class)) {
            return (T) new GetNearbyActivitiesViewModel(GetNearbyActRepository.getInstance(new GetNearbyActDataSource()), executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
