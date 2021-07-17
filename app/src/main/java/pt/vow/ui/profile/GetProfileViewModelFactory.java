package pt.vow.ui.profile;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import pt.vow.data.getProfile.GetProfileDataSource;
import pt.vow.data.getProfile.GetProfileRepository;
import pt.vow.data.rating.GetRatingDataSource;
import pt.vow.data.rating.GetRatingRepository;

public class GetProfileViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public GetProfileViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetProfileViewModel.class)) {
            return (T) new GetProfileViewModel(GetProfileRepository.getInstance(new GetProfileDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
