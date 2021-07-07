package pt.vow.ui.activityInfo;

import androidx.annotation.Nullable;

public class SetRatingResult {
    @Nullable
    private RatingView success;
    @Nullable
    private Integer error;

    SetRatingResult(@Nullable Integer error) {
        this.error = error;
    }

    SetRatingResult(@Nullable RatingView success) {
        this.success = success;
    }

    @Nullable
    RatingView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
