package pt.vow.data.update;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;

public class UpdateRepository {
    private static volatile UpdateRepository instance;

    private UpdateDataSource dataSource;

    private RegisteredUser user = null;

    private UpdateRepository(UpdateDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static UpdateRepository getInstance(UpdateDataSource dataSource) {
        if (instance == null) {
            instance = new UpdateRepository(dataSource);
        }
        return instance;
    }

    public boolean isUpdated() {
        return user != null;
    }

    private void setUpdatedUser(RegisteredUser user) {
        this.user = user;
    }

    public Result<RegisteredUser> update(String name, String password, String newPassword, String phoneNumber) {
        Result<RegisteredUser> result = dataSource.update(name, password, newPassword, phoneNumber);
        if (result instanceof Result.Success) {
            setUpdatedUser(((Result.Success<RegisteredUser>) result).getData());
        }
        return result;
    }

}

