package pt.vow.data.model;

import java.util.List;

public class ActivitiesList {

    List<Activity> activities;

    public void ActivitiesList(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() { return activities; }
}
