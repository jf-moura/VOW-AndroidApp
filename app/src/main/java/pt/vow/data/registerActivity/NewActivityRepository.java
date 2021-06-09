package pt.vow.data.registerActivity;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;

public class NewActivityRepository {

    private static volatile NewActivityRepository instance;

    private NewActivityDataSource dataSource;

    private RegisteredActivity user = null;

    private NewActivityRepository(NewActivityDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static NewActivityRepository getInstance(NewActivityDataSource dataSource) {
        if (instance == null) {
            instance = new NewActivityRepository(dataSource);
        }
        return instance;
    }

    public boolean isRegistered() {
        return user != null;
    }

    public void deleteActivity() {
        user = null;
        dataSource.deleteActivity();
    }

    private void setRegisteredActivity(RegisteredActivity user) {
        this.user = user;
    }

    public Result<RegisteredActivity> registerActivity(String username, String tokenID, String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes) {
        Result<RegisteredActivity> result = dataSource.registerActivity(username, tokenID, name, address, coordinates, time,type, participantNum, durationInMinutes);
        if (result instanceof Result.Success) {
            setRegisteredActivity(((Result.Success<RegisteredActivity>) result).getData());
        }
        return result;
    }

}
