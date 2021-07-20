package pt.vow.data.comments;

import pt.vow.data.Result;
import pt.vow.ui.comments.CommentsRegisteredView;
import pt.vow.ui.comments.RegisterCommentView;

public class UpdateCommentRepository {
    private static volatile UpdateCommentRepository instance;

    private UpdateCommentDataSource dataSource;

    private RegisterCommentView comment = null;

    private UpdateCommentRepository(UpdateCommentDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static UpdateCommentRepository getInstance(UpdateCommentDataSource dataSource) {
        if (instance == null) {
            instance = new UpdateCommentRepository(dataSource);
        }
        return instance;
    }

    private void setComment(RegisterCommentView comment) {
        this.comment = comment;
    }

    public Result<RegisterCommentView> updateComment(String username, String tokenID, String commentID, String activityOwner, String activityID, String comment) {
        Result<RegisterCommentView> result = dataSource.updateComment(username, tokenID, commentID, activityOwner, activityID, comment);
        if (result instanceof Result.Success) {
            setComment(((Result.Success<RegisterCommentView>) result).getData());
        }
        return result;
    }
}
