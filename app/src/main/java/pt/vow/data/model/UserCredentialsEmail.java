package pt.vow.data.model;

public class UserCredentialsEmail {

    // Class with the parameters for the input REST service
// They are translated to JSON by RETROFIT2 converter

    String email;
    String password;

    public UserCredentialsEmail(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}

