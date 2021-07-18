package pt.vow.ui.comments;

import java.io.Serializable;
import java.util.List;

import pt.vow.data.model.Activity;
import pt.vow.data.model.Commentary;

public class CommentsRegisteredView implements Serializable {
    List<Commentary> comments;

    public CommentsRegisteredView(List<Commentary> comments) {
        this.comments = comments;
    }

    public List<Commentary> getComments() { return comments; }
}
