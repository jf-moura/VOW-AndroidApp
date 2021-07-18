package pt.vow.data.comments;

import pt.vow.data.Result;
import pt.vow.ui.comments.CommentsRegisteredView;

public class GetActCommentsRepository {
    private static volatile GetActCommentsRepository instance;

    private GetActCommentsDataSource dataSource;

    private CommentsRegisteredView comments = null;

    private GetActCommentsRepository(GetActCommentsDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetActCommentsRepository getInstance(GetActCommentsDataSource dataSource) {
        if (instance == null) {
            instance = new GetActCommentsRepository(dataSource);
        }
        return instance;
    }
    public boolean hasComments() {
        return comments != null;
    }

    private void setGetActComments(CommentsRegisteredView comments) {
        this.comments = comments;
    }

    public Result<CommentsRegisteredView> getActComments(String username, String tokenID, String activityOwner, String activityID) {
        Result<CommentsRegisteredView> result = dataSource.getActComments(username, tokenID, activityOwner, activityID);
        if (result instanceof Result.Success) {
            setGetActComments(((Result.Success<CommentsRegisteredView>) result).getData());
        }
        return result;
    }
}
