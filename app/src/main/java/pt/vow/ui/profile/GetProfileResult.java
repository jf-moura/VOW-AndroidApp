package pt.vow.ui.profile;

import androidx.annotation.Nullable;

public class GetProfileResult {
    @Nullable
    private ProfileInfoView success;
    @Nullable
    private Integer error;

    GetProfileResult(@Nullable Integer error) {
        this.error = error;
    }

    GetProfileResult(@Nullable ProfileInfoView success) {
        this.success = success;
    }

    @Nullable
    public ProfileInfoView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
