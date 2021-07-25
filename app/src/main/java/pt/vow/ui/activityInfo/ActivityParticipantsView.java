package pt.vow.ui.activityInfo;

import java.util.List;

import pt.vow.data.activityParticipants.ActivityParticipantsDataSource;
import pt.vow.data.model.Activity;

public class ActivityParticipantsView {
    String activityID;
    List<String> participants;

    public ActivityParticipantsView(String activityID, List<String> participants) {
        this.activityID = activityID;
        this.participants = participants;
    }

    public String getActivityID() {
        return activityID;
    }

    public List<String> getParticipants() { return participants; }

}
