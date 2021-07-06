package pt.vow.data.getActivitiesByUser;

import pt.vow.data.Result;
import pt.vow.ui.profile.ActivitiesByUserView;

public class GetActivitiesByUserRepository {

    private static volatile GetActivitiesByUserRepository instance;

    private GetActivitiesByUserDataSource dataSource;

    private ActivitiesByUserView activities = null;

    private GetActivitiesByUserRepository(GetActivitiesByUserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetActivitiesByUserRepository getInstance(GetActivitiesByUserDataSource dataSource) {
        if (instance == null) {
            instance = new GetActivitiesByUserRepository(dataSource);
        }
        return instance;
    }

    public boolean hasActivities() {
        return activities != null;
    }

    private void setActivities(ActivitiesByUserView activities) {
        this.activities = activities;
    }

    public Result<ActivitiesByUserView> getActivitiesByUser(String username, String tokenID) {
        Result<ActivitiesByUserView> result = dataSource.getActivitiesByUser(username, tokenID);
        if (result instanceof Result.Success) {
            setActivities(((Result.Success<ActivitiesByUserView>) result).getData());
        }
        return result;
    }
}
