package pt.vow.data.model;

public class UserInfo {
    String name;
    String username;
    String email;
    String phoneNumber;
    String dateBirth;
    String bio;
    String website;

    public UserInfo(String name, String username, String email, String phoneNumber, String dateBirth, String bio, String website) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateBirth = dateBirth;
        this.bio = bio;
        this.website = website;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public String getBio() {
        return bio;
    }

    public String getWebsite() {
        return website;
    }
}
