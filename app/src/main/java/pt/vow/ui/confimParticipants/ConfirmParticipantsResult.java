package pt.vow.ui.confimParticipants;

import androidx.annotation.Nullable;

public class ConfirmParticipantsResult {
    @Nullable
    private ParticipantsConfirmed success;
    @Nullable
    private Integer error;

    ConfirmParticipantsResult(@Nullable Integer error) {
        this.error = error;
    }

    ConfirmParticipantsResult(@Nullable ParticipantsConfirmed success) {
        this.success = success;
    }

    @Nullable
    ParticipantsConfirmed getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
