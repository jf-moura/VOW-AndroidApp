package pt.vow.ui.comments;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.concurrent.Executor;

import pt.vow.data.comments.DeleteCommentDataSource;
import pt.vow.data.comments.DeleteCommentRepository;

public class DeleteCommentViewModelFactory implements ViewModelProvider.Factory {
    private Executor executor;

    public DeleteCommentViewModelFactory(Executor executor) {
        this.executor = executor;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DeleteCommentViewModel.class)) {
            return (T) new DeleteCommentViewModel(DeleteCommentRepository.getInstance(new DeleteCommentDataSource()),executor);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
