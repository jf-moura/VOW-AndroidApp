package pt.vow.ui.confimParticipants;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import pt.vow.data.activityParticipants.ActivityParticipantsDataSource;
import pt.vow.data.activityParticipants.ActivityParticipantsRepository;
import pt.vow.data.activityParticipants.ConfirmParticipantsDataSource;
import pt.vow.data.activityParticipants.ConfirmParticipantsRepository;

public class ParticipantsConfirmedViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public ParticipantsConfirmedViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ParticipantsConfirmedViewModel.class)) {
            return (T) new ParticipantsConfirmedViewModel(ActivityParticipantsRepository.getInstance(new ActivityParticipantsDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
