package pt.vow.data.registerActivity;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.ui.newActivity.RegisteredActivityView;

public class NewActivityRepository {

    private static volatile NewActivityRepository instance;

    private NewActivityDataSource dataSource;

    private RegisteredActivityView user = null;

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
    }

    private void setRegisteredActivity(RegisteredActivityView user) {
        this.user = user;
    }

    public Result<RegisteredActivityView> registerActivity(String username, String tokenID, String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes, String description) {
        Result<RegisteredActivityView> result = dataSource.registerActivity(username, tokenID, name, address, coordinates, time,type, participantNum, durationInMinutes, description);
        if (result instanceof Result.Success) {
            setRegisteredActivity(((Result.Success<RegisteredActivityView>) result).getData());
        }
        return result;
    }

}
