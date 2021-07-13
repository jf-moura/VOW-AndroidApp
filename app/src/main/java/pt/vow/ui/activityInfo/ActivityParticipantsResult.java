package pt.vow.ui.activityInfo;

import androidx.annotation.Nullable;

public class ActivityParticipantsResult {
    @Nullable
    private ActivityParticipantsView success;
    @Nullable
    private Integer error;

    ActivityParticipantsResult(@Nullable Integer error) {
        this.error = error;
    }

    ActivityParticipantsResult(@Nullable ActivityParticipantsView success) {
        this.success = success;
    }

    @Nullable
    public ActivityParticipantsView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
