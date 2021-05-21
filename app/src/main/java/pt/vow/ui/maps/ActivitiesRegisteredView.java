package pt.vow.ui.maps;

import java.io.Serializable;
import java.util.List;

import pt.vow.data.model.Activity;

public class ActivitiesRegisteredView  implements Serializable {

    List<Activity> activities;

    public ActivitiesRegisteredView(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() { return activities; }

}
