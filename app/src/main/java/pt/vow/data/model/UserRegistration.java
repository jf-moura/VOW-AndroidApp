package pt.vow.data.model;

public class UserRegistration {
    String name;
    String username;
    String email;
    String password;
    String phoneNumber;
    String website;
    String dateBirth;
    String role;

    public void UserRegistration(String name, String username, String email, String password, String phoneNumber, String website, String dateBirth, String role) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.website = website;
        this.dateBirth = dateBirth;
        this.role = role;
    }

    public String getName() { return name; }

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

    public String getWebsite() { return website; }

    public String getDateBirth() { return dateBirth; }

    public String getRole() { return  role; }
}
