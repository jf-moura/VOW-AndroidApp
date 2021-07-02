package pt.vow.ui.enroll;

import androidx.annotation.Nullable;

public class CancelEnrollResult {
    @Nullable
    private CancelEnrolledActivityView success;
    @Nullable
    private Integer error;

    CancelEnrollResult(@Nullable Integer error) {
        this.error = error;
    }

    CancelEnrollResult(@Nullable CancelEnrolledActivityView success) {
        this.success = success;
    }

    @Nullable
    CancelEnrolledActivityView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
