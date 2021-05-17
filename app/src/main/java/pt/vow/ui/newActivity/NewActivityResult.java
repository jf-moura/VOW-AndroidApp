package pt.vow.ui.newActivity;

import androidx.annotation.Nullable;

public class NewActivityResult {
    @Nullable
    private NewActivityView success;
    @Nullable
    private Integer error;

    NewActivityResult(@Nullable Integer error) {
        this.error = error;
    }

    NewActivityResult(@Nullable NewActivityView success) {
        this.success = success;
    }

    @Nullable
    NewActivityView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
