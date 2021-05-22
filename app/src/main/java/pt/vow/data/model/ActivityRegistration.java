package pt.vow.data.model;

public class ActivityRegistration {

    String username;
    String tokenID;
    String name;
    String address;
    String coordinates;
    String time;
    String participantNum;
    String durationInMinutes;

    public ActivityRegistration(String username, String tokenID, String name, String address, String coordinates, String time, String participantNum, String durationInMinutes) {
        this.username = username;
        this.tokenID = tokenID;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.time = time;
        this.participantNum = participantNum;
        this.durationInMinutes = durationInMinutes;
    }

    public String getUsername() { return username; }

    public String getTokenID() { return tokenID; }

    public String getName() { return name; }

    public String getAddress() {
        return address;
    }

    public String getCoordinates() { return coordinates; }

    public String getTime() {
        return time;
    }

    public String getParticipantNum() {
        return participantNum;
    }

    public String getDurationInMinutes() { return durationInMinutes; }
}
