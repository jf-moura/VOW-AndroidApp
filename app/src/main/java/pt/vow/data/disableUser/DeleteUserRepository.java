package pt.vow.data.disableUser;

import pt.vow.data.Result;
import pt.vow.data.model.DeleteRegisteredUser;

public class DeleteUserRepository {
    private static volatile DeleteUserRepository instance;

    private DeleteUserDataSource dataSource;

    private DeleteRegisteredUser delete;

    private DeleteUserRepository(DeleteUserDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static DeleteUserRepository getInstance(DeleteUserDataSource dataSource) {
        if (instance == null) {
            instance = new DeleteUserRepository(dataSource);
        }
        return instance;
    }

    private void setDeleteUser(DeleteRegisteredUser delete) {
        this.delete = delete;
    }

    public Result<DeleteRegisteredUser> deleteUser(String username, String tokenID, String userToDelete) {
        Result<DeleteRegisteredUser> result = dataSource.deleteUser(username, tokenID, userToDelete);
        if (result instanceof Result.Success) {
            setDeleteUser((((Result.Success<DeleteRegisteredUser>) result).getData()));
        }
        return result;
    }
}
