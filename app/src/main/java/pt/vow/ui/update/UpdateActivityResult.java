package pt.vow.ui.update;

import androidx.annotation.Nullable;

import pt.vow.ui.newActivity.RegisteredActivityView;

public class UpdateActivityResult {
    @Nullable
    private RegisteredActivityView success;
    @Nullable
    private Integer error;

    UpdateActivityResult(@Nullable Integer error) {
        this.error = error;
    }

    UpdateActivityResult(@Nullable RegisteredActivityView success) {
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
