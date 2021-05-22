package pt.vow.data.model;

public class EnrollActivityCredentials {
    String username;
    String tokenID;
    String activityOwner;
    String activityID;

    public EnrollActivityCredentials(String username, String tokenID, String activityOwner,
                                     String activityID) {
        this.username = username;
        this.tokenID = tokenID;
        this.activityOwner = activityOwner;
        this.activityID = activityID;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenID() {
        return tokenID;
    }

    public String getActivityOwner() {
        return activityOwner;
    }

    public String getActivityID() {
        return activityID;
    }
}
