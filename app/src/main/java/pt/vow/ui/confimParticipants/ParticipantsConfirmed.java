package pt.vow.ui.confimParticipants;

import java.util.List;

public class ParticipantsConfirmed {
    String activityID;
    List<String> participants;

    public ParticipantsConfirmed(String activityID, List<String> participants) {
        this.activityID = activityID;
        this.participants = participants;
    }

    public String getActivityID() {
        return activityID;
    }

    public List<String> getParticipants() {
        return participants;
    }
}
