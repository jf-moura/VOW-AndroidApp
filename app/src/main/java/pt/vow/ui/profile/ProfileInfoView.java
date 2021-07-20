package pt.vow.ui.profile;

import java.io.Serializable;

import pt.vow.ui.mainPage.Image;

public class ProfileInfoView implements Serializable {
    private String username, tokenID, name, email, phoneNumber, dateBirth, bio, website;
    private boolean visibility, status;

    public ProfileInfoView(String username, String tokenID, String name, String email, String phoneNumber, String dateBirth, String bio, String website, boolean visibility, boolean status) {
        this.username = username;
        this.tokenID = tokenID;
        this.name = name;
        this.email = email;
        this.phoneNumber=phoneNumber;
        this.dateBirth=dateBirth;
        this.bio=bio;
        this.website = website;
        this.visibility = visibility;
        this.status = status;
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

    public boolean getVisibility() { return visibility; }

    public boolean getStatus() { return status; }
}
