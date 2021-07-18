package pt.vow.ui.comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.cancelEnroll.CancelEnrollRepository;
import pt.vow.data.comments.RegisterCommentRepository;
import pt.vow.data.model.CancelEnrolledActivity;
import pt.vow.data.model.CommentMadeByUser;
import pt.vow.ui.enroll.CancelEnrollResult;
import pt.vow.ui.enroll.CancelEnrolledActivityView;

public class RegisterCommentViewModel extends ViewModel {
    private MutableLiveData<RegisterCommentResult> registerCommentResult = new MutableLiveData<>();
    private RegisterCommentRepository registerCommentRepository;

    private final Executor executor;

    RegisterCommentViewModel(RegisterCommentRepository registerCommentRepository, Executor executor) {
        this.registerCommentRepository = registerCommentRepository;
        this.executor = executor;
    }

    public LiveData<RegisterCommentResult> getRegisterCommentResult() {
        return registerCommentResult;
    }

    public void registerComment(String username, String tokenID, String activityOwner, String activityID, String comment) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<CommentMadeByUser> result = registerCommentRepository.registerComment(username, tokenID, activityOwner, activityID, comment);
                if (result instanceof Result.Success) {
                    CommentMadeByUser data = ((Result.Success<CommentMadeByUser>) result).getData();
                    registerCommentResult.postValue(new RegisterCommentResult(new RegisterCommentView(data.getDisplayName())));
                } else {
                    registerCommentResult.postValue(new RegisterCommentResult(R.string.register_comment_failed));
                }
            }
        });
    }
}
