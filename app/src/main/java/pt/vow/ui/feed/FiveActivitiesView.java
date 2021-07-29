package pt.vow.ui.feed;

import java.util.List;

import pt.vow.data.model.Activity;

public class FiveActivitiesView {
    private List<Activity> activities;

    public FiveActivitiesView(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return activities;
    }
}
