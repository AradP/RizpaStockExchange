package bl;

import models.User;

import java.util.ArrayList;

public class UserManager {
    // static variable single_instance of type Singleton
    private static UserManager single_instance = null;

    private ArrayList<User> users;

    public UserManager() {
        this.users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    // static method to create instance of Singleton class
    public static UserManager getInstance() {
        if (single_instance == null)
            single_instance = new UserManager();

        return single_instance;
    }
}
