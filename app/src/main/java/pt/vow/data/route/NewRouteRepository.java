package pt.vow.data.route;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredRoute;
import pt.vow.data.registerActivity.NewActivityDataSource;
import pt.vow.data.registerActivity.NewActivityRepository;

public class NewRouteRepository {
    private static volatile NewRouteRepository instance;

    private NewRouteDataSource dataSource;

    private RegisteredRoute user = null;


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

    private void setRegisteredRoute(RegisteredRoute user) {
        this.user = user;
    }

    public Result<RegisteredRoute> registerRoute(String username, String tokenID, String name, String address, String time, String type, String participantNum, String durationInMinutes, String[] coordinateArray) {
        Result<RegisteredRoute> result = dataSource.registerRoute(username, tokenID, name, address,time,type, participantNum, durationInMinutes, coordinateArray);
        if (result instanceof Result.Success) {
            setRegisteredRoute(((Result.Success<RegisteredRoute>) result).getData());
        }
        return result;
    }
}
