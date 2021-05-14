package pt.vow.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegistrationEntity {

    String name;
    String username;
    String email;
    String password;
    String phoneNumber;
    String website;

    public UserRegistrationEntity(String name, String username, String email, String password, String phoneNumber, String website) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.website = website;

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

}
