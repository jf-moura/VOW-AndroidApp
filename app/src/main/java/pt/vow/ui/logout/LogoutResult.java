package pt.vow.ui.logout;

import androidx.annotation.Nullable;

import pt.vow.ui.logout.LoggedOutUserView;


class LogoutResult {
    @Nullable
    private LoggedOutUserView success;
    @Nullable
    private Integer error;

    LogoutResult(@Nullable Integer error) {
        this.error = error;
    }

    LogoutResult(@Nullable LoggedOutUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedOutUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
