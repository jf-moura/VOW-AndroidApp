package pt.vow.data.model;

public class ActivityUpdate {

    String name;
    String address;
    String coordinates;
    String time;
    String type;
    String participantNum;
    String durationInMinutes;
    String coordinateArray;
    String append;
    String role;
    String description;

    public ActivityUpdate(String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes, String coordinateArray, String append, String role, String description) {
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.time = time;
        this.type = type;
        this.participantNum = participantNum;
        this.durationInMinutes = durationInMinutes;
        this.coordinateArray = coordinateArray;
        this.append = append;
        this.role = role;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getParticipantNum() {
        return participantNum;
    }

    public String getDurationInMinutes() {
        return durationInMinutes;
    }

    public String getCoordinateArray() {
        return coordinateArray;
    }

    public String getAppend() {
        return append;
    }

    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }
}
