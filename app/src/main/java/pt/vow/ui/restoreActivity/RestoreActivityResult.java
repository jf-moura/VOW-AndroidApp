package pt.vow.ui.restoreActivity;

import androidx.annotation.Nullable;

import pt.vow.ui.newActivity.RegisteredActivityView;

public class RestoreActivityResult {
    @Nullable
    private RegisteredActivityView success;
    @Nullable
    private Integer error;

    RestoreActivityResult(@Nullable Integer error) {
        this.error = error;
    }

    RestoreActivityResult(@Nullable RegisteredActivityView success) {
        this.success = success;
    }

    @Nullable
    public RegisteredActivityView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}

