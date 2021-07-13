package pt.vow.data.route;

import pt.vow.data.Result;
import pt.vow.data.getActivitiesByUser.GetActivitiesByUserDataSource;
import pt.vow.data.getActivitiesByUser.GetActivitiesByUserRepository;
import pt.vow.ui.maps.RouteCoordinatesView;
import pt.vow.ui.profile.ActivitiesByUserView;

public class GetRouteCoordRepository {
    private static volatile GetRouteCoordRepository instance;

    private GetRouteCoordDataSource dataSource;

    private RouteCoordinatesView coordinates = null;

    private GetRouteCoordRepository(GetRouteCoordDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetRouteCoordRepository getInstance(GetRouteCoordDataSource dataSource) {
        if (instance == null) {
            instance = new GetRouteCoordRepository(dataSource);
        }
        return instance;
    }

    public boolean hasCoordinates() {
        return coordinates != null;
    }

    private void setCoordinates(RouteCoordinatesView coordinates) {
        this.coordinates = coordinates;
    }

    public Result<RouteCoordinatesView> getRouteCoordinates(String username, String tokenID, String activityOwner, String activityID) {
        Result<RouteCoordinatesView> result = dataSource.getRouteCoord(username, tokenID, activityOwner, activityID);
        if (result instanceof Result.Success) {
            setCoordinates(((Result.Success<RouteCoordinatesView>) result).getData());
        }
        return result;
    }
}