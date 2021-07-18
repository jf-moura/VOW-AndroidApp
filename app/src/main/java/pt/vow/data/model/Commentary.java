package pt.vow.data.model;

import java.io.Serializable;

public class Commentary implements Serializable {
    String commentID;
    String commentOwner;
    String comment;
    String creationTime;
    String lastModificationTime;

    public Commentary(String commentID, String commentOwner, String comment, String creationTime, String lastModificationTime) {
        this.commentID = commentID;
        this.commentOwner = commentOwner;
        this.comment = comment;
        this.creationTime = creationTime;
        this.lastModificationTime = lastModificationTime;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getCommentOwner() {
        return commentOwner;
    }

    public String getComment() {
        return comment;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public String getLastModificationTime() {
        return lastModificationTime;
    }
}
