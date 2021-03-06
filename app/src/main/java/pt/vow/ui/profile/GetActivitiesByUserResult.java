package pt.vow.ui.profile;

import androidx.annotation.Nullable;

public class GetActivitiesByUserResult {
    @Nullable
    private ActivitiesByUserView success;
    @Nullable
    private Integer error;

    GetActivitiesByUserResult(@Nullable Integer error) {
        this.error = error;
    }

    GetActivitiesByUserResult(@Nullable ActivitiesByUserView success) {
        this.success = success;
    }

    @Nullable
    public ActivitiesByUserView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
