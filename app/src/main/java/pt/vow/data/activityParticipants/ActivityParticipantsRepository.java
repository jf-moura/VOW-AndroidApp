package pt.vow.data.activityParticipants;

import pt.vow.data.Result;
import pt.vow.ui.activityInfo.ActivityParticipantsView;

public class ActivityParticipantsRepository {
    private static volatile ActivityParticipantsRepository instance;

    private ActivityParticipantsDataSource dataSource;

    private ActivityParticipantsView participants = null;

    private ActivityParticipantsRepository(ActivityParticipantsDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ActivityParticipantsRepository getInstance(ActivityParticipantsDataSource dataSource) {
        if (instance == null) {
            instance = new ActivityParticipantsRepository(dataSource);
        }
        return instance;
    }

    public boolean hasParticipants() {
        return participants != null;
    }

    private void setParticipants(ActivityParticipantsView participants) {
        this.participants = participants;
    }

    public Result<ActivityParticipantsView> getParticipants(String username, String tokenID, boolean presetOnly, String owner, String activityID) {
        Result<ActivityParticipantsView> result = dataSource.getActParticipants(username, tokenID, presetOnly, owner, activityID);
        if (result instanceof Result.Success) {
           setParticipants(((Result.Success<ActivityParticipantsView>) result).getData());
        }
        return result;
    }

}
