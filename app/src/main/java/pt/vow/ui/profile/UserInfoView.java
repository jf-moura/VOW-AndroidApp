package pt.vow.ui.profile;

public class UserInfoView {
    private String username, tokenID, name, email, phoneNumber, dateBirth, bio, website;

    public UserInfoView(String username, String tokenID, String name, String email, String phoneNumber, String dateBirth, String bio, String website) {
        this.username = username;
        this.tokenID = tokenID;
        this.name = name;
        this.email = email;
        this.phoneNumber=phoneNumber;
        this.dateBirth=dateBirth;
        this.bio=bio;
        this.website = website;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenID() {
        return tokenID;
    }

    public String getName() {
        return name;
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
