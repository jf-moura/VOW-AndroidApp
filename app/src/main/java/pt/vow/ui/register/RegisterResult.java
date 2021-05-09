package pt.vow.ui.register;

import androidx.annotation.Nullable;

public class RegisterResult {
    @Nullable
    private RegisteredUserView success;
    @Nullable
    private Integer error;

    RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterResult(@Nullable RegisteredUserView success) {
        this.success = success;
    }


    @Nullable
    RegisteredUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
