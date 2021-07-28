package pt.vow.data.getNearbyActivities;

import pt.vow.data.Result;
import pt.vow.data.model.NearbyActivitiesView;

public class GetNearbyActRepository {
    private static volatile GetNearbyActRepository instance;

    private GetNearbyActDataSource dataSource;

    private NearbyActivitiesView activities = null;

    private GetNearbyActRepository(GetNearbyActDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetNearbyActRepository getInstance(GetNearbyActDataSource dataSource) {
        if (instance == null) {
            instance = new GetNearbyActRepository(dataSource);
        }
        return instance;
    }

    public boolean hasActivities() {
        return activities != null;
    }

    private void setActivities(NearbyActivitiesView activities) {
        this.activities = activities;
    }

    public Result<NearbyActivitiesView> getNearbyActivities(String username, String tokenID, String p1Long, String p1Lat, String p2Long, String p2Lat) {
        Result<NearbyActivitiesView> result = dataSource.getNearbyActivities(username, tokenID, p1Long, p1Lat, p2Long, p2Lat);
        if (result instanceof Result.Success) {
            setActivities(((Result.Success<NearbyActivitiesView>) result).getData());
        }
        return result;
    }
}
