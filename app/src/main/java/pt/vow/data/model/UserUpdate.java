package pt.vow.data.model;

public class UserUpdate {
    String name;
    String username;
    String tokenID;
    String oldPassword;
    String password;
    String phoneNumber;
    String dateBirth;
    String bio;
    String website;

    public UserUpdate(String username, String tokenID, String name, String oldPassword, String password, String phoneNumber, String dateBirth, String bio, String website) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateBirth = dateBirth;
        this.website = website;
        this.tokenID = tokenID;
        this.bio = bio;
        this.oldPassword = oldPassword;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenID() {
        return tokenID;
    }

    public String getPassword() {
        return password;
    }
    public String getOldPassword() {
        return oldPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public String getWebsite() {
        return website;
    }
    public String getBio(){return bio;}
}
