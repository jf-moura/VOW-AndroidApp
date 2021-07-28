package pt.vow.data.updateActivity;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredActivity;
import pt.vow.data.model.RegisteredUser;
import pt.vow.data.update.UpdateDataSource;
import pt.vow.ui.update.UpdateActivity;

public class UpdateActivityRepository {
    private static volatile UpdateActivityRepository instance;

    private UpdateActivityDataSource dataSource;

    private RegisteredActivity activity = null;

    private UpdateActivityRepository(UpdateActivityDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static UpdateActivityRepository getInstance(UpdateActivityDataSource dataSource) {
        if (instance == null) {
            instance = new UpdateActivityRepository(dataSource);
        }
        return instance;
    }

    public boolean isUpdated() {
        return activity != null;
    }

    private void setUpdatedActivity(RegisteredActivity activity) {
        this.activity = activity;
    }

    public Result<RegisteredActivity> updateActivity(String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes, String coordinateArray, Boolean append, String role, String description) {
        Result<RegisteredActivity> result = dataSource.updateActivity(name, address, coordinates, time, type, participantNum, durationInMinutes, coordinateArray, append, role, description);
        if (result instanceof Result.Success) {
            setUpdatedActivity(((Result.Success<RegisteredActivity>) result).getData());
        }
        return result;
    }

}

