package pt.vow.ui.comments;

import androidx.annotation.Nullable;

public class DeleteCommentResult {
    @Nullable
    private RegisterCommentView success;
    @Nullable
    private Integer error;

    DeleteCommentResult(@Nullable Integer error) {
        this.error = error;
    }

    DeleteCommentResult(@Nullable RegisterCommentView success) {
        this.success = success;
    }

    @Nullable
    RegisterCommentView getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }

}
