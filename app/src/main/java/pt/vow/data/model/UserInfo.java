package pt.vow.data.model;

import pt.vow.ui.image.Image;

public class UserInfo {
    String name;
    String username;
    String email;
    String phoneNumber;
    String dateBirth;
    String bio;
    String website;
    String creationTime;
    Boolean visibility;
    Integer role;
    Boolean status;
    Integer score;
    Image image;


    public UserInfo(String name, String username, String email, String phoneNumber, String dateBirth, String bio, String website, String creationTime, Boolean visibility, Integer role, Boolean status,  Integer score) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateBirth = dateBirth;
        this.bio = bio;
        this.website = website;
        this.creationTime = creationTime;
        this.visibility = visibility;
        this.role = role;
        this.status = status;
        this.score = score;
        this.image = null;
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

    public String getCreationTime() {
        return creationTime;
    }

    public Boolean getVisibility() {
        return visibility;
    }

    public Integer getRole() {
        return role;
    }

    public Boolean getStatus() {
        return status;
    }

    public Integer getScore() {
        return score;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
