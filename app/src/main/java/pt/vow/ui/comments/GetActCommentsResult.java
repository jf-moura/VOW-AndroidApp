package pt.vow.ui.comments;

import androidx.annotation.Nullable;

import pt.vow.ui.feed.ActivitiesRegisteredView;

public class GetActCommentsResult {
    @Nullable
    private CommentsRegisteredView success;
    @Nullable
    private Integer error;

    GetActCommentsResult(@Nullable Integer error) {
        this.error = error;
    }

    GetActCommentsResult(@Nullable CommentsRegisteredView success) {
        this.success = success;
    }

    @Nullable
    public CommentsRegisteredView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}
