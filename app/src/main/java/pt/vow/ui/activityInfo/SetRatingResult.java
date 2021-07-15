package pt.vow.ui.activityInfo;

import androidx.annotation.Nullable;

public class SetRatingResult {
    @Nullable
    private SetRatingView success;
    @Nullable
    private Integer error;

    SetRatingResult(@Nullable Integer error) {
        this.error = error;
    }

    SetRatingResult(@Nullable SetRatingView success) {
        this.success = success;
    }

    @Nullable
    SetRatingView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
