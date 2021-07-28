package pt.vow.data.model;

import java.io.Serializable;
import java.util.List;

public class NearbyActivitiesView implements Serializable {
    public List<Activity> activities;

    public NearbyActivitiesView(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }

}
