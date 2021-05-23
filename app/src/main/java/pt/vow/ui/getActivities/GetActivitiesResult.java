package pt.vow.ui.getActivities;

import androidx.annotation.Nullable;

public class GetActivitiesResult {
    @Nullable
    private ActivitiesRegisteredView success;
    @Nullable
    private Integer error;

    GetActivitiesResult(@Nullable Integer error) {
        this.error = error;
    }

    GetActivitiesResult(@Nullable ActivitiesRegisteredView success) {
        this.success = success;
    }

    @Nullable
    ActivitiesRegisteredView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
