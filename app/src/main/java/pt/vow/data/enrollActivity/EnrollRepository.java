package pt.vow.data.enrollActivity;

import pt.vow.data.Result;
import pt.vow.data.model.EnrolledActivity;

public class EnrollRepository {
    private static volatile EnrollRepository instance;

    private EnrollDataSource dataSource;

    private EnrolledActivity enroll;

    private EnrollRepository(EnrollDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static EnrollRepository getInstance(EnrollDataSource dataSource) {
        if (instance == null) {
            instance = new EnrollRepository(dataSource);
        }
        return instance;
    }

    private void setEnrollInActivity(EnrolledActivity enroll) {
        this.enroll = enroll;
    }

    public Result<EnrolledActivity> enrollInActivity(String username, String tokenID, String activityOwner, String activityID) {
        Result<EnrolledActivity> result = dataSource.enrollActivity(username, tokenID, activityOwner, activityID);
        if (result instanceof Result.Success) {
            setEnrollInActivity(((Result.Success<EnrolledActivity>) result).getData());
        }
        return result;
    }

}
