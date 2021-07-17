package pt.vow.ui.disableUser;

import androidx.annotation.Nullable;

public class DeleteUserResult {
    @Nullable
    private DeleteUserView success;
    @Nullable
    private Integer error;

    DeleteUserResult(@Nullable Integer error) {
        this.error = error;
    }

    DeleteUserResult(@Nullable DeleteUserView success) {
        this.success = success;
    }

    @Nullable
    DeleteUserView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
