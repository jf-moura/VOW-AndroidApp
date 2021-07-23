package pt.vow.data.model;

import java.io.Serializable;
import java.util.List;

import pt.vow.ui.image.Image;


public class Activity implements Serializable {

    String owner;
    String id;
    String name;
    String address;
    String coordinates;
    String time;
    String participantNum;
    String durationInMinutes;
    String type;
    boolean status;
    List<String> participants;
    Image image;

    public Activity(String owner, String id, String name, String address, String coordinates, String time, String type, String participantNum, boolean status, String durationInMinutes) {
        this.owner = owner;
        this.id = id;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.time = time;
        this.type = type;
        this.participantNum = participantNum;
        this.durationInMinutes = durationInMinutes;
        this.status = status;
        this.participants = null;
        this.image = null;
    }

    public String getOwner() {
        return owner;
    }

    public String getId() {
        return id;
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

    public String getParticipantNum() {
        return participantNum;
    }

    public String getDurationInMinutes() {
        return durationInMinutes;
    }

    public String getType() {
        return type;
    }

    public boolean getStatus() { return status; }

    public void addParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }
}
