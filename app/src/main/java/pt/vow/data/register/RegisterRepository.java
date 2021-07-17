package pt.vow.data.register;

import pt.vow.data.Result;
import pt.vow.data.model.RegisteredUser;

public class RegisterRepository {

    private static volatile RegisterRepository instance;

    private RegisterDataSource dataSource;

    private RegisteredUser user = null;

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
    }

    public Result<RegisteredUser> registerOrganization(String name, String username, String email, String password, String phoneNumber, String website, Boolean visibility, String bio) {
        Result<RegisteredUser> result = dataSource.registerOrganization(name, username, email, password, phoneNumber, website, visibility, bio);
        if (result instanceof Result.Success) {
            setRegisteredUser(((Result.Success<RegisteredUser>) result).getData());
        }
        return result;
    }

    public Result<RegisteredUser> registerVolunteer(String name, String username, String email, String password, String phoneNumber, String dateBirth, Boolean visibility, String bio) {
        Result<RegisteredUser> result = dataSource.registerVolunteer(name, username, email, password, phoneNumber, dateBirth, visibility, bio);
        if (result instanceof Result.Success) {
            setRegisteredUser(((Result.Success<RegisteredUser>) result).getData());
        }
        return result;
    }
}
