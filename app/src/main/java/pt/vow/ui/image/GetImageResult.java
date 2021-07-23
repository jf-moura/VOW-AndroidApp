package pt.vow.ui.image;

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
    public Image getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
