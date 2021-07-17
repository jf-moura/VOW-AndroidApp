package pt.vow.data.disableActivity;

import pt.vow.data.Result;
import pt.vow.data.model.CancelEnrolledActivity;
import pt.vow.data.model.DeleteCreatedActivity;

public class DeleteActivityRepository {
    private static volatile DeleteActivityRepository instance;

    private DeleteActivityDataSource dataSource;

    private DeleteCreatedActivity delete;

    private DeleteActivityRepository(DeleteActivityDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DeleteActivityRepository getInstance(DeleteActivityDataSource dataSource) {
        if (instance == null) {
            instance = new DeleteActivityRepository(dataSource);
        }
        return instance;
    }

    private void setDeleteActivity(DeleteCreatedActivity delete) {
        this.delete = delete;
    }

    public Result<DeleteCreatedActivity> deleteActivity(String username, String tokenID, String activityOwner, String activityID) {
        Result<DeleteCreatedActivity> result = dataSource.deleteActivity(username, tokenID, activityOwner, activityID);
        if (result instanceof Result.Success) {
            setDeleteActivity(((Result.Success<DeleteCreatedActivity>) result).getData());
        }
        return result;
    }
}
