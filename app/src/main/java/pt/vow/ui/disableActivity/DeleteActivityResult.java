package pt.vow.ui.disableActivity;

import androidx.annotation.Nullable;

public class DeleteActivityResult {
    @Nullable
    private DeleteActivityView success;
    @Nullable
    private Integer error;

    DeleteActivityResult(@Nullable Integer error) {
        this.error = error;
    }

    DeleteActivityResult(@Nullable DeleteActivityView success) {
        this.success = success;
    }

    @Nullable
    public DeleteActivityView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
