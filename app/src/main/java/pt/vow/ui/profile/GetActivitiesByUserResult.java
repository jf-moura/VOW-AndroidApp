package pt.vow.ui.profile;

import androidx.annotation.Nullable;

public class GetActivitiesByUserResult {
    @Nullable
    private ActivitiesByUserRegisteredView success;
    @Nullable
    private Integer error;

    GetActivitiesByUserResult(@Nullable Integer error) {
        this.error = error;
    }

    GetActivitiesByUserResult(@Nullable ActivitiesByUserRegisteredView success) {
        this.success = success;
    }

    @Nullable
    ActivitiesByUserRegisteredView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
