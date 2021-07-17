package pt.vow.ui.profile;

import androidx.annotation.Nullable;

public class GetProfileResult {
    @Nullable
    private UserInfoView success;
    @Nullable
    private Integer error;

    GetProfileResult(@Nullable Integer error) {
        this.error = error;
    }

    GetProfileResult(@Nullable UserInfoView success) {
        this.success = success;
    }

    @Nullable
    public UserInfoView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
