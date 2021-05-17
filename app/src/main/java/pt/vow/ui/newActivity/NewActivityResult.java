package pt.vow.ui.newActivity;

import androidx.annotation.Nullable;

public class NewActivityResult {
    @Nullable
    private RegisteredActivityView success;
    @Nullable
    private Integer error;

    NewActivityResult(@Nullable Integer error) {
        this.error = error;
    }

    NewActivityResult(@Nullable RegisteredActivityView success) {
        this.success = success;
    }

    @Nullable
    RegisteredActivityView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
