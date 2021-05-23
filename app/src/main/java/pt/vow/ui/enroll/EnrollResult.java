package pt.vow.ui.enroll;

import androidx.annotation.Nullable;


public class EnrollResult {
    @Nullable
    private EnrolledActivityView success;
    @Nullable
    private Integer error;

    EnrollResult(@Nullable Integer error) {
        this.error = error;
    }

    EnrollResult(@Nullable EnrolledActivityView success) {
        this.success = success;
    }

    @Nullable
    EnrolledActivityView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}
