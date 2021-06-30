package pt.vow.data.getActivities;

import pt.vow.data.Result;
import pt.vow.ui.getActivities.ActivitiesRegisteredView;

public class GetActivitiesRepository {

    private static volatile GetActivitiesRepository instance;

    private GetActivitiesDataSource dataSource;

    private ActivitiesRegisteredView activities = null;

    private GetActivitiesRepository(GetActivitiesDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetActivitiesRepository getInstance(GetActivitiesDataSource dataSource) {
        if (instance == null) {
            instance = new GetActivitiesRepository(dataSource);
        }
        return instance;
    }

    public boolean hasActivities() {
        return activities != null;
    }

    private void setActivities(ActivitiesRegisteredView activities) {
        this.activities = activities;
    }

    public Result<ActivitiesRegisteredView> getActivities(String username, String tokenID) {
        Result<ActivitiesRegisteredView> result = dataSource.getActivities(username, tokenID);
        if (result instanceof Result.Success) {
            setActivities(((Result.Success<ActivitiesRegisteredView>) result).getData());
        }
        return result;
    }

}
