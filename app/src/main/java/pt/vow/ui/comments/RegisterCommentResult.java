package pt.vow.ui.comments;

import androidx.annotation.Nullable;


public class RegisterCommentResult {
    @Nullable
    private RegisterCommentView success;
    @Nullable
    private Integer error;

    RegisterCommentResult(@Nullable Integer error) {
        this.error = error;
    }

    RegisterCommentResult(@Nullable RegisterCommentView success) {
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
