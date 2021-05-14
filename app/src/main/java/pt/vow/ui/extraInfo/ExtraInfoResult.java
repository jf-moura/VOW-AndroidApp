package pt.vow.ui.extraInfo;

import androidx.annotation.Nullable;



class ExtraInfoResult {
    @Nullable
    private ExtraInfoView success;
    @Nullable
    private Integer error;

    ExtraInfoResult(@Nullable Integer error) {
        this.error = error;
    }

    ExtraInfoResult(@Nullable ExtraInfoView success) {
        this.success = success;
    }

    @Nullable
    ExtraInfoView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
