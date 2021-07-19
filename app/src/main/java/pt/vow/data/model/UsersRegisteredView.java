package pt.vow.data.model;

import java.io.Serializable;
import java.util.List;

public class UsersRegisteredView implements Serializable {
    public List<UserInfo> users;

    public UsersRegisteredView(List<UserInfo> users) {
        this.users = users;
    }

    public List<UserInfo> getUsers() { return users; }

}
