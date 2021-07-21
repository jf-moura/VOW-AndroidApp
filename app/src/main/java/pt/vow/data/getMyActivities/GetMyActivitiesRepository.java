package pt.vow.data.getMyActivities;

import pt.vow.data.Result;
import pt.vow.ui.profile.MyActivitiesView;

public class GetMyActivitiesRepository {

    private static volatile GetMyActivitiesRepository instance;

    private GetMyActivitiesDataSource dataSource;

    private MyActivitiesView activities = null;

    private GetMyActivitiesRepository(GetMyActivitiesDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetMyActivitiesRepository getInstance(GetMyActivitiesDataSource dataSource) {
        if (instance == null) {
            instance = new GetMyActivitiesRepository(dataSource);
        }
        return instance;
    }

    public boolean hasActivities() {
        return activities != null;
    }

    private void setActivities(MyActivitiesView activities) {
        this.activities = activities;
    }

    public Result<MyActivitiesView> getMyActivities(String userToGet, String username, String tokenID) {
        Result<MyActivitiesView> result = dataSource.getMyActivities(userToGet, username, tokenID);
        if (result instanceof Result.Success) {
            setActivities(((Result.Success<MyActivitiesView>) result).getData());
        }
        return result;
    }
}
