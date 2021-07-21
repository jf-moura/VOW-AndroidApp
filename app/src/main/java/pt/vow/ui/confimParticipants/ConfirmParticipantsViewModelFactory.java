package pt.vow.ui.confimParticipants;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.activityParticipants.ConfirmParticipantsDataSource;
import pt.vow.data.activityParticipants.ConfirmParticipantsRepository;

public class ConfirmParticipantsViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public ConfirmParticipantsViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ConfirmParticipantsViewModel.class)) {
            return (T) new ConfirmParticipantsViewModel(ConfirmParticipantsRepository.getInstance(new ConfirmParticipantsDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
