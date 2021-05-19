package pt.vow.ui.update;

import androidx.annotation.Nullable;

public class UpdateResult {
    @Nullable
    private UpdatedUserView success;
    @Nullable
    private Integer error;

    UpdateResult(@Nullable Integer error) {
        this.error = error;
    }

    UpdateResult(@Nullable UpdatedUserView success) {
        this.success = success;
    }

    @Nullable
    UpdatedUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
