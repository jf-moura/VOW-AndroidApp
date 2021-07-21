package pt.vow.data.model;

import java.util.List;

public class ConfirmParticipants {

    List<String> participants;

    public ConfirmParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipants() {
        return participants;
    }
}
