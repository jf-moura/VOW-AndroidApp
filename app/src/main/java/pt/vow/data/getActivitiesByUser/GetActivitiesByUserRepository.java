package pt.vow.data.getActivitiesByUser;

import pt.vow.data.Result;
import pt.vow.data.getActivities.GetActivitiesDataSource;
import pt.vow.data.getActivities.GetActivitiesRepository;
import pt.vow.ui.getActivities.ActivitiesRegisteredView;
import pt.vow.ui.profile.ActivitiesByUserRegisteredView;

public class GetActivitiesByUserRepository {

    private static volatile GetActivitiesByUserRepository instance;

    private GetActivitiesByUserDataSource dataSource;

    private ActivitiesByUserRegisteredView activities = null;

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

    private void setActivities(ActivitiesByUserRegisteredView activities) {
        this.activities = activities;
    }

    public Result<ActivitiesByUserRegisteredView> getActivitiesByUser(String username, String tokenID) {
        Result<ActivitiesByUserRegisteredView> result = dataSource.getActivitiesByUser(username, tokenID);
        if (result instanceof Result.Success) {
            setActivities(((Result.Success<ActivitiesByUserRegisteredView>) result).getData());
        }
        return result;
    }
}
