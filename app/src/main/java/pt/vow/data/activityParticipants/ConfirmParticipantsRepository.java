package pt.vow.data.activityParticipants;

import java.util.List;

import pt.vow.data.Result;
import pt.vow.ui.activityInfo.ActivityParticipantsView;
import pt.vow.ui.confimParticipants.ParticipantsConfirmed;

public class ConfirmParticipantsRepository {
    private static volatile ConfirmParticipantsRepository instance;

    private ConfirmParticipantsDataSource dataSource;

    private ParticipantsConfirmed participants = null;

    private ConfirmParticipantsRepository(ConfirmParticipantsDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ConfirmParticipantsRepository getInstance(ConfirmParticipantsDataSource dataSource) {
        if (instance == null) {
            instance = new ConfirmParticipantsRepository(dataSource);
        }
        return instance;
    }

    public boolean hasConfirmedParticipants() {
        return participants != null;
    }

    private void setParticipantsConfirmed(ParticipantsConfirmed participants) {
        this.participants = participants;
    }

    public Result<ParticipantsConfirmed> confirmParticipants(String username, String tokenID, String activityID, List<String> participants) {
        Result<ParticipantsConfirmed> result = dataSource.confirmParticipants(username, tokenID, activityID, participants);
        if (result instanceof Result.Success) {
            setParticipantsConfirmed(((Result.Success<ParticipantsConfirmed>) result).getData());
        }
        return result;
    }
}
