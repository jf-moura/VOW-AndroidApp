package pt.vow.ui.profile;

import androidx.annotation.Nullable;

public class GetMyActivitiesResult {
    @Nullable
    private MyActivitiesView success;
    @Nullable
    private Integer error;

    GetMyActivitiesResult(@Nullable Integer error) {
        this.error = error;
    }

    GetMyActivitiesResult(@Nullable MyActivitiesView success) {
        this.success = success;
    }

    @Nullable
    public MyActivitiesView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
