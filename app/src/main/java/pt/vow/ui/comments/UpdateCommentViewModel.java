package pt.vow.ui.comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.comments.RegisterCommentRepository;
import pt.vow.data.comments.UpdateCommentRepository;
import pt.vow.data.model.CommentMadeByUser;
import pt.vow.data.model.Commentary;

public class UpdateCommentViewModel extends ViewModel {
    private MutableLiveData<UpdateCommentResult> updateCommentResult = new MutableLiveData<>();
    private UpdateCommentRepository updateCommentRepository;

    private final Executor executor;

    UpdateCommentViewModel(UpdateCommentRepository updateCommentRepository, Executor executor) {
        this.updateCommentRepository = updateCommentRepository;
        this.executor = executor;
    }

    public LiveData<UpdateCommentResult> getUpdateCommentResult() {
        return updateCommentResult;
    }

    public void updateComment(String username, String tokenID, String commentID, String activityOwner, String activityID, String comment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
            Result<RegisterCommentView> result = updateCommentRepository.updateComment(username, tokenID, commentID, activityOwner, activityID, comment);
                if (result instanceof Result.Success) {
                    RegisterCommentView data = ((Result.Success<RegisterCommentView>) result).getData();
                    updateCommentResult.postValue(new UpdateCommentResult(new RegisterCommentView(data.getDisplayName())));
                } else {
                    updateCommentResult.postValue(new UpdateCommentResult(R.string.register_comment_failed));
                    updateCommentResult.postValue(new UpdateCommentResult(R.string.register_comment_failed));
                }
            }
        });
    }
}
