package pt.vow.data.getAllUsers;

import pt.vow.data.Result;
import pt.vow.data.getActivities.GetActivitiesDataSource;
import pt.vow.data.getActivities.GetActivitiesRepository;
import pt.vow.data.model.UsersRegisteredView;
import pt.vow.ui.feed.ActivitiesRegisteredView;

public class GetAllUsersRepository {
    private static volatile GetAllUsersRepository instance;

    private GetAllUsersDataSource dataSource;

    private UsersRegisteredView users = null;

    private GetAllUsersRepository(GetAllUsersDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static GetAllUsersRepository getInstance(GetAllUsersDataSource dataSource) {
        if (instance == null) {
            instance = new GetAllUsersRepository(dataSource);
        }
        return instance;
    }

    public boolean hasUsers() {
        return users != null;
    }

    private void setUsers(UsersRegisteredView users) {
        this.users = users;
    }

    public Result<UsersRegisteredView> getUsers(String username, String tokenID) {
        Result<UsersRegisteredView> result = dataSource.getAllUsers(username, tokenID);
        if (result instanceof Result.Success) {
            setUsers(((Result.Success<UsersRegisteredView>) result).getData());
        }
        return result;
    }
}
