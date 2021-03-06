package pt.vow.data.model;

public class UserRegistrationVolunteer {
    String name;
    String username;
    String email;
    String password;
    String phoneNumber;
    String dateBirth;
    Boolean visibility;
    String bio;

    public UserRegistrationVolunteer(String name, String username, String email, String password, String phoneNumber, String dateBirth, Boolean visibility, String bio) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateBirth = dateBirth;
        this.visibility = visibility;
        this.bio = bio;
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

    public Boolean getVisibility() {
        return visibility;
    }

    public String getBio() {
        return bio;
    }

}


