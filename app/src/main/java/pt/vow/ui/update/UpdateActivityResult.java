package pt.vow.ui.update;

import androidx.annotation.Nullable;

import pt.vow.data.model.RegisteredActivity;
import pt.vow.ui.newActivity.RegisteredActivityView;

public class UpdateActivityResult {
    @Nullable
    private RegisteredActivity success;
    @Nullable
    private Integer error;

    UpdateActivityResult(@Nullable Integer error) {
        this.error = error;
    }

    UpdateActivityResult(@Nullable RegisteredActivity success) {
        this.success = success;
    }

    @Nullable
    RegisteredActivity getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
