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
    public ActivitiesByUserRegisteredView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
