package pt.vow.data.register;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;

public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    private RegisteredUser user = null;

    // private constructor : singleton access
    private RegisterRepository(RegisterDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static RegisterRepository getInstance(RegisterDataSource dataSource) {
        if (instance == null) {
            instance = new RegisterRepository(dataSource);
        }
        return instance;
    }

    public boolean isRegistered() {
        return user != null;
    }

    public void deleteRegistrarion() {
        user = null;
        dataSource.deleteRegistration();
    }

    private void setRegisteredUser(RegisteredUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public Result<RegisteredUser> register(String name, String username, String email, String password, String phoneNumber, String website, String dateBirth, String role) {
        Result<RegisteredUser> result = dataSource.register(name, username, email, password, phoneNumber, website, dateBirth, role);
        if (result instanceof Result.Success) {
            setRegisteredUser(((Result.Success<RegisteredUser>) result).getData());
        }
        return result;
    }
}
