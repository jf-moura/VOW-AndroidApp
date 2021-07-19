package pt.vow.ui.getAllUsers;

import androidx.annotation.Nullable;

import pt.vow.data.model.UsersRegisteredView;

public class GetAllUsersResult {
    @Nullable
    private UsersRegisteredView success;
    @Nullable
    private Integer error;

    GetAllUsersResult(@Nullable Integer error) {
        this.error = error;
    }

    GetAllUsersResult(@Nullable UsersRegisteredView success) {
        this.success = success;
    }

    @Nullable
    UsersRegisteredView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
