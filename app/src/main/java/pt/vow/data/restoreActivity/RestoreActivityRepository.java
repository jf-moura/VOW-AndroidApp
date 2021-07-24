package pt.vow.data.restoreActivity;

import pt.vow.data.Result;

public class RestoreActivityRepository {
    private static volatile RestoreActivityRepository instance;

    private RestoreActivityDataSource dataSource;

    private RestoreActivityRepository(RestoreActivityDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RestoreActivityRepository getInstance(RestoreActivityDataSource dataSource) {
        if (instance == null) {
            instance = new RestoreActivityRepository(dataSource);
        }
        return instance;
    }

    public Result<Void> restoreActivity(String username, String tokenID, String activityOwner, String activityID) {
        Result<Void> result = dataSource.restoreActivity(username, tokenID, activityOwner, activityID);
        return result;
    }
}
