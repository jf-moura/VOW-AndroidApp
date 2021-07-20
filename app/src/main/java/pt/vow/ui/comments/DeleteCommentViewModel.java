package pt.vow.ui.comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.comments.DeleteCommentRepository;
import pt.vow.data.disableActivity.DeleteActivityRepository;
import pt.vow.data.model.DeleteCreatedActivity;
import pt.vow.ui.disableActivity.DeleteActivityResult;
import pt.vow.ui.disableActivity.DeleteActivityView;

public class DeleteCommentViewModel extends ViewModel {
    private MutableLiveData<DeleteCommentResult> deleteCommentResult = new MutableLiveData<>();
    private DeleteCommentRepository deleteCommentRepository;

    private final Executor executor;

    DeleteCommentViewModel(DeleteCommentRepository deleteCommentRepository, Executor executor) {
        this.deleteCommentRepository = deleteCommentRepository;
        this.executor = executor;
    }

    public LiveData<DeleteCommentResult> getDeleteCommentResult() {
        return deleteCommentResult;
    }

    public void deleteComment(String username, String tokenID, String commentID, String commentOwner, String activityOwner, String activityID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<Void> result = deleteCommentRepository.deleteComment(username, tokenID, commentID, commentOwner, activityOwner, activityID);
                if (result instanceof Result.Success) {
                    RegisterCommentView data = ((Result.Success<RegisterCommentView>) result).getData();
                    deleteCommentResult.postValue(new DeleteCommentResult(new RegisterCommentView(data.getDisplayName())));
                } else {
                    deleteCommentResult.postValue(new DeleteCommentResult(R.string.delete_comment_failed));
                }
            }
        });
    }
}
