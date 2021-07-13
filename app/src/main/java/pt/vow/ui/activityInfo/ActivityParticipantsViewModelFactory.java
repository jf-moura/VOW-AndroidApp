package pt.vow.ui.activityInfo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.activityParticipants.ActivityParticipantsDataSource;
import pt.vow.data.activityParticipants.ActivityParticipantsRepository;

public class ActivityParticipantsViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public ActivityParticipantsViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ActivityParticipantsViewModel.class)) {
            return (T) new ActivityParticipantsViewModel(ActivityParticipantsRepository.getInstance(new ActivityParticipantsDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
