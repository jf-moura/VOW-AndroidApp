package pt.vow.ui.maps;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.data.getActivities.GetActivitiesRepository;

public class MapsViewModel extends ViewModel {
    private GetActivitiesRepository getActivitiesRepository;
    private final Executor executor;

    public MapsViewModel(GetActivitiesRepository getActivitiesRepository, Executor executor) {
        this.getActivitiesRepository = getActivitiesRepository;
        this.executor = executor;
    }
}
