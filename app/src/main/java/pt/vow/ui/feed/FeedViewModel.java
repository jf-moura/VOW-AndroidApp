package pt.vow.ui.feed;

import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.data.getActivities.GetActivitiesRepository;

public class FeedViewModel extends ViewModel {

    private GetActivitiesRepository getActivitiesRepository;
    private final Executor executor;

    public FeedViewModel(GetActivitiesRepository getActivitiesRepository, Executor executor) {
        this.getActivitiesRepository = getActivitiesRepository;
        this.executor = executor;
    }
}

