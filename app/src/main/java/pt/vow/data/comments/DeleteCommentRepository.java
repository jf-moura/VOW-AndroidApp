package pt.vow.data.comments;

import pt.vow.data.Result;

public class DeleteCommentRepository {
    private static volatile DeleteCommentRepository instance;

    private DeleteCommentDataSource dataSource;

    private DeleteCommentRepository(DeleteCommentDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DeleteCommentRepository getInstance(DeleteCommentDataSource dataSource) {
        if (instance == null) {
            instance = new DeleteCommentRepository(dataSource);
        }
        return instance;
    }


    public Result<Void> deleteComment(String username, String tokenID, String commentID, String commentOwner, String activityID, String activityOwner) {
        Result<Void> result = dataSource.deleteComment(username, tokenID, commentID, commentOwner, activityID, activityOwner);
        return result;
    }
}
