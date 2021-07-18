package pt.vow.ui.comments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.comments.RegisterCommentDataSource;
import pt.vow.data.comments.RegisterCommentRepository;

public class RegisterCommentViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public RegisterCommentViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(RegisterCommentViewModel.class)) {
            return (T) new RegisterCommentViewModel(RegisterCommentRepository.getInstance(new RegisterCommentDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
