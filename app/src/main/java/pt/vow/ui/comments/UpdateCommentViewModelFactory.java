package pt.vow.ui.comments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.comments.RegisterCommentDataSource;
import pt.vow.data.comments.RegisterCommentRepository;
import pt.vow.data.comments.UpdateCommentDataSource;
import pt.vow.data.comments.UpdateCommentRepository;

public class UpdateCommentViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public UpdateCommentViewModelFactory(Executor executor) {
        this.executor = executor;
    }
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UpdateCommentViewModel.class)) {
            return (T) new UpdateCommentViewModel(UpdateCommentRepository.getInstance(new UpdateCommentDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}