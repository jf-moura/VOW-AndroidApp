package pt.vow.data.cancelEnroll;

import pt.vow.data.Result;
import pt.vow.data.model.CancelEnrolledActivity;

public class CancelEnrollRepository {
    private static volatile CancelEnrollRepository instance;

    private CancelEnrollDataSource dataSource;

    private CancelEnrolledActivity enroll;

    private CancelEnrollRepository(CancelEnrollDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static CancelEnrollRepository getInstance(CancelEnrollDataSource dataSource) {
        if (instance == null) {
            instance = new CancelEnrollRepository(dataSource);
        }
        return instance;
    }

    private void setCancelEnrollInActivity(CancelEnrolledActivity enroll) {
        this.enroll = enroll;
    }

    public Result<CancelEnrolledActivity> cancelEnrollInActivity(String username, String tokenID, String activityOwner, String activityID) {
        Result<CancelEnrolledActivity> result = dataSource.cancelEnroll(username, tokenID, activityOwner, activityID);
        if (result instanceof Result.Success) {
            setCancelEnrollInActivity(((Result.Success<CancelEnrolledActivity>) result).getData());
        }
        return result;
    }
}
