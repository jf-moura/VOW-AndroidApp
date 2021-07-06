package pt.vow.data.route;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.registerActivity.NewActivityDataSource;
import pt.vow.data.registerActivity.NewActivityRepository;

public class NewRouteRepository {
    private static volatile NewRouteRepository instance;

    private NewRouteDataSource dataSource;

    private RegisteredActivity user = null;

    private NewRouteRepository(NewRouteDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static NewRouteRepository getInstance(NewRouteDataSource dataSource) {
        if (instance == null) {
            instance = new NewRouteRepository(dataSource);
        }
        return instance;
    }

    public boolean isRegistered() {
        return user != null;
    }

    public void deleteRoute() {
        user = null;
        dataSource.deleteRoute();
    }

    private void setRegisteredActivity(RegisteredActivity user) {
        this.user = user;
    }

    public Result<RegisteredActivity> registerRoute(String username, String tokenID, String name, String address, String time, String type, String participantNum, String durationInMinutes, String[] coordinateArray) {
        Result<RegisteredActivity> result = dataSource.registerRoute(username, tokenID, name, address,time,type, participantNum, durationInMinutes, coordinateArray);
        if (result instanceof Result.Success) {
            setRegisteredActivity(((Result.Success<RegisteredActivity>) result).getData());
        }
        return result;
    }
}
