package pt.vow.data.comments;

import pt.vow.data.Result;
import pt.vow.data.model.CommentMadeByUser;

public class RegisterCommentRepository {
    private static volatile RegisterCommentRepository instance;

    private RegisterCommentDataSource dataSource;

    private CommentMadeByUser registerComment;

    private RegisterCommentRepository(RegisterCommentDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterCommentRepository getInstance(RegisterCommentDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterCommentRepository(dataSource);
        }
        return instance;
    }

    private void setRegisterComment(CommentMadeByUser registerComment) {
        this.registerComment = registerComment;
    }

    public Result<CommentMadeByUser> registerComment(String username, String tokenID, String activityOwner, String activityID, String comment) {
        Result<CommentMadeByUser> result = dataSource.registerComment(username, tokenID, activityOwner, activityID, comment);
        if (result instanceof Result.Success) {
            setRegisterComment(((Result.Success<CommentMadeByUser>) result).getData());
        }
        return result;
    }
}
