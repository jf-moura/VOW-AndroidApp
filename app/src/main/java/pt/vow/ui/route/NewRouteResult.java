package pt.vow.ui.route;

import androidx.annotation.Nullable;

import pt.vow.ui.newActivity.RegisteredActivityView;

public class NewRouteResult {
    @Nullable
    private RegisteredActivityView success;
    @Nullable
    private Integer error;

    NewRouteResult(@Nullable Integer error) {
        this.error = error;
    }

    NewRouteResult(@Nullable RegisteredActivityView success) {
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
