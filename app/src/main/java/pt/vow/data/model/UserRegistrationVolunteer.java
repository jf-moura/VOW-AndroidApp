package pt.vow.data.model;

public class UserRegistrationVolunteer {
    String name;
    String username;
    String email;
    String password;
    String phoneNumber;
    String dateBirth;

    public UserRegistrationVolunteer(String name, String username, String email, String password, String phoneNumber, String dateBirth) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateBirth = dateBirth;
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
        return dateBirth;
    }

}


