package pt.vow.ui.feed;

import androidx.annotation.Nullable;

public class GetFiveActivitiesResult {
    @Nullable
    private FiveActivitiesView success;
    @Nullable
    private Integer error;

    GetFiveActivitiesResult(@Nullable Integer error) {
        this.error = error;
    }

    GetFiveActivitiesResult(@Nullable FiveActivitiesView success) {
        this.success = success;
    }

    @Nullable
    FiveActivitiesView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

}

