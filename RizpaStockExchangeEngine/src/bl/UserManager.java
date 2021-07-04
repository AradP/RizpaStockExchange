package bl;

import models.Stock;
import models.User;

import java.util.ArrayList;
import java.util.Optional;

public class UserManager {
    // static variable single_instance of type Singleton
    private static UserManager single_instance = null;

    // static method to create instance of Singleton class
    public static UserManager getInstance() {
        if (single_instance == null)
            single_instance = new UserManager();

        return single_instance;
    }

    private ArrayList<User> users;

    private UserManager() {
        this.users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
