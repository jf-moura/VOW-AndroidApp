package pt.vow.data.model;

import android.graphics.Bitmap;

import java.io.Serializable;

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
    byte[] image;

    public Activity(String owner, String id, String name, String address, String coordinates, String time, String type, String participantNum, String durationInMinutes) {
        this.owner = owner;
        this.id = id;
        this.name = name;
        this.address = address;
        this.coordinates = coordinates;
        this.time = time;
        this.type = type;
        this.participantNum = participantNum;
        this.durationInMinutes = durationInMinutes;
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

    public byte[] getImage() { return image; }

    public void setImage(byte[] img) { image = img; }

}
