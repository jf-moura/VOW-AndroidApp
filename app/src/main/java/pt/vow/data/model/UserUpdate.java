package pt.vow.data.model;

public class UserUpdate {
    String name;
    String username;
    String tokenID;
    String password;
    String phoneNumber;
    String dateBirth;
    String website;

    public UserUpdate(String username, String tokenID, String name, String password, String phoneNumber, String dateBirth, String website) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.dateBirth = dateBirth;
        this.website = website;
        this.tokenID=tokenID;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDateBirth() {
        return dateBirth;
    }

    public String getWebsite(){return website;}
}
