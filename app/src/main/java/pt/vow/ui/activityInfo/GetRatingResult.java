package pt.vow.ui.activityInfo;

import androidx.annotation.Nullable;

public class GetRatingResult {
    @Nullable
    private GetRatingView success;
    @Nullable
    private Integer error;

    GetRatingResult(@Nullable Integer error) {
        this.error = error;
    }

    GetRatingResult(@Nullable GetRatingView success) {
        this.success = success;
    }

    @Nullable
    public GetRatingView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
