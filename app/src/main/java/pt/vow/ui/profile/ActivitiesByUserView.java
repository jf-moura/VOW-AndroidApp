package pt.vow.ui.profile;

import java.io.Serializable;
import java.util.List;

import pt.vow.data.model.Activity;

public class ActivitiesByUserView implements Serializable {

    List<Activity> activities;
    String userToGet;

    public ActivitiesByUserView(String userToGet, List<Activity> activities) {
        this.userToGet = userToGet;
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public String getUserToGet() {
        return userToGet;
    }
}
