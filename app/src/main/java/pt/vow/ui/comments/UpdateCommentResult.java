package pt.vow.ui.comments;

import androidx.annotation.Nullable;

public class UpdateCommentResult {
    @Nullable
    private RegisterCommentView success;
    @Nullable
    private Integer error;

    UpdateCommentResult(@Nullable Integer error) {
        this.error = error;
    }

    UpdateCommentResult(@Nullable RegisterCommentView success) {
        this.success = success;
    }

    @Nullable
    public RegisterCommentView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
