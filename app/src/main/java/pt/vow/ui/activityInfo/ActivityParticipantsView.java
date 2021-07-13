package pt.vow.ui.activityInfo;

import java.util.List;

import pt.vow.data.activityParticipants.ActivityParticipantsDataSource;
import pt.vow.data.model.Activity;

public class ActivityParticipantsView {
    List<String> participants;

    public ActivityParticipantsView(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipants() { return participants; }

}
