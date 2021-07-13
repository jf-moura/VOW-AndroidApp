package pt.vow.data.model;

import java.util.List;

public class RouteRegistration {
    String username;
    String tokenID;
    String name;
    String time;
    String type;
    String participantNum;
    String durationInMinutes;
    List<String> coordinateArray;
    String address;


    public RouteRegistration(String username, String tokenID, String name, String address, String time, String type, String participantNum, String durationInMinutes, List<String> coordinateArray) {
        this.username = username;
        this.tokenID = tokenID;
        this.name = name;
        this.time = time;
        this.participantNum = participantNum;
        this.durationInMinutes = durationInMinutes;
        this.type = type;
        this.coordinateArray = coordinateArray;
        this.address = address;
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

    public List<String> getCoordinatesArray() {
        return coordinateArray;
    }

    public String getTime() {
        return time;
    }

    public String getParticipantNum() {
        return participantNum;
    }

    public String getDurationInMinutes() {
        return durationInMinutes;
    }

    public String getType() {
        return type;
    }

    public String getAddress(){return address;}
}
