package pt.vow.data.model;

public class UserRegistrationPerson {
    String name;
    String username;
    String email;
    String password;
    String phoneNumber;
    String birthDateString;

    public UserRegistrationPerson(String name, String username, String email, String password, String phoneNumber, String birthDateString) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.birthDateString = birthDateString;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDateBirth() {
        return birthDateString;
    }

}


