package pt.vow.data.model;

// Class with the parameters for the input REST service
// They are translated to JSON by RETROFIT2 converter
public class UserCredentials {
    String username;
    String password;

    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}


