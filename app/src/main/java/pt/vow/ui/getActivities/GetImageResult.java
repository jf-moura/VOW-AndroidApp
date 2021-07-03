package pt.vow.ui.getActivities;

import androidx.annotation.Nullable;

public class GetImageResult {
    @Nullable
    private Image success;
    @Nullable
    private Integer error;

    GetImageResult(@Nullable Integer error) {
        this.error = error;
    }

    GetImageResult(@Nullable Image success) { this.success = success; }

    @Nullable
    Image getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
