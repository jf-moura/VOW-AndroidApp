package pt.vow.ui.profile;

import java.io.Serializable;
import java.util.List;

import pt.vow.data.model.Activity;

public class ActivitiesByUserView implements Serializable {

    List<Activity> activities;

    public ActivitiesByUserView(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
