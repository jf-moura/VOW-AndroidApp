package pt.vow.data.registerActivity;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.register.RegisterDataSource;
import pt.vow.data.register.RegisterRepository;

public class NewActivityRepository {

    private static volatile NewActivityRepository instance;

    private NewActivityDataSource dataSource;

    private RegisteredActivity user = null;

    // private constructor : singleton access
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
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<RegisteredActivity> registerActivity(String username, String tokenID, String name, String address, String time, String participantNum, String durationInMinutes) {
        Result<RegisteredActivity> result = dataSource.registerActivity(username, tokenID, name, address, time, participantNum, durationInMinutes);
        if (result instanceof Result.Success) {
            setRegisteredActivity(((Result.Success<RegisteredActivity>) result).getData());
        }
        return result;
    }

}
