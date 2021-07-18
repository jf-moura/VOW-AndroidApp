package pt.vow.ui.comments;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.concurrent.Executor;

import pt.vow.R;
import pt.vow.data.Result;
import pt.vow.data.comments.GetActCommentsRepository;
import pt.vow.data.model.Commentary;

public class GetActCommentsViewModel extends ViewModel {
    private MutableLiveData<GetActCommentsResult> getActCommentsResult = new MutableLiveData<>();
    private MutableLiveData<List<Commentary>> comments = new MutableLiveData<>();
    private GetActCommentsRepository getActCommentsRepository;
    private final Executor executor;

    public GetActCommentsViewModel(GetActCommentsRepository getActCommentsRepository, Executor executor) {
        this.getActCommentsRepository = getActCommentsRepository;
        this.executor = executor;
    }

    public LiveData<GetActCommentsResult> getActCommentsResult() {
        return getActCommentsResult;
    }


    public void getActComments(String username, String tokenID, String activityOwner, String activityID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Result<CommentsRegisteredView> result = getActCommentsRepository.getActComments(username, tokenID, activityOwner, activityID);
                if (result instanceof Result.Success) {
                    CommentsRegisteredView data = ((Result.Success<CommentsRegisteredView>) result).getData();
                    comments.postValue(data.getComments());
                    getActCommentsResult.postValue(new GetActCommentsResult(new CommentsRegisteredView(data.getComments())));
                } else {
                    getActCommentsResult.postValue(new GetActCommentsResult(R.string.get_comments_failed));
                }
            }
        });
    }

    public LiveData<List<Commentary>> getActCommentsList() {
        return comments;
    }
}
