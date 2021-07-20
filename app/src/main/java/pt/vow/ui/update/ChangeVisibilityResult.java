package pt.vow.ui.update;

import androidx.annotation.Nullable;

public class ChangeVisibilityResult {
    @Nullable
    private UpdatedUserView success;
    @Nullable
    private Integer error;

    ChangeVisibilityResult(@Nullable Integer error) {
        this.error = error;
    }

    ChangeVisibilityResult(@Nullable UpdatedUserView success) {
        this.success = success;
    }

    @Nullable
    public UpdatedUserView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
