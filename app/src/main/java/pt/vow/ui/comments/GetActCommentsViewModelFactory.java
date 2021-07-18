package pt.vow.ui.comments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.comments.GetActCommentsDataSource;
import pt.vow.data.comments.GetActCommentsRepository;

public class GetActCommentsViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public GetActCommentsViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(GetActCommentsViewModel.class)) {
            return (T) new GetActCommentsViewModel(GetActCommentsRepository.getInstance(new GetActCommentsDataSource()), executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
