package pt.vow.data.logout;

import pt.vow.data.Result;
import pt.vow.data.login.LoginDataSource;
import pt.vow.data.login.LoginRepository;
import pt.vow.data.model.LoggedInUser;
import pt.vow.data.model.LoggedOutUser;

public class LogoutRepository {

    private static volatile LogoutRepository instance;

    private LogoutDataSource dataSource;

    private LoggedOutUser user = null;

    private LogoutRepository(LogoutDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LogoutRepository getInstance(LogoutDataSource dataSource) {
        if (instance == null) {
            instance = new LogoutRepository(dataSource);
        }
        return instance;
    }

    public Result<LoggedOutUser> logout(String username, String tokenID) {
        Result<LoggedOutUser> result = dataSource.logout(username, tokenID);
        if (result instanceof Result.Success) {
            setLoggedOutUser(((Result.Success<LoggedOutUser>) result).getData());
        }
        return result;
    }

    private void setLoggedOutUser(LoggedOutUser user) {
        this.user = user;

    }
}
