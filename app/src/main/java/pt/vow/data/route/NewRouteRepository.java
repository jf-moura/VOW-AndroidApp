package pt.vow.data.route;

import java.util.List;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredRoute;
import pt.vow.data.registerActivity.NewActivityDataSource;
import pt.vow.data.registerActivity.NewActivityRepository;
import pt.vow.ui.newActivity.RegisteredActivityView;

public class NewRouteRepository {
    private static volatile NewRouteRepository instance;

    private NewRouteDataSource dataSource;

    private RegisteredActivityView user = null;


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

    private void setRegisteredRoute(RegisteredActivityView user) {
        this.user = user;
    }

    public Result<RegisteredActivityView> registerRoute(String username, String tokenID, String name, String address, String time, String type, String participantNum, String durationInMinutes, List<String> coordinateArray, String description) {
        Result<RegisteredActivityView> result = dataSource.registerRoute(username, tokenID, name, address,time,type, participantNum, durationInMinutes, coordinateArray, description);
        if (result instanceof Result.Success) {
            setRegisteredRoute(((Result.Success<RegisteredActivityView>) result).getData());
        }
        return result;
    }
}
