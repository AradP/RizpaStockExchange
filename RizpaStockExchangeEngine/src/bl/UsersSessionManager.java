package bl;

import enums.Role;
import exceptions.users.UserAlreadyExistsException;
import models.User;

import java.util.ArrayList;

public class UsersSessionManager {
    // static variable single_instance of type Singleton
    private static UsersSessionManager single_instance = null;

    // static method to create instance of Singleton class
    public static UsersSessionManager getInstance() {
        if (single_instance == null)
            single_instance = new UsersSessionManager();

        return single_instance;
    }

    // current logged in users
    private static ArrayList<User> users;

    private UsersSessionManager() {
        this.users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User login(String username, Role role) throws UserAlreadyExistsException {
        if (!isLoggedIn(username)) {
            User user = new User(username, role);
            users.add(user);

            return user;
        }

        throw new UserAlreadyExistsException(username);
    }

    private boolean isLoggedIn(String username) {
        return users.stream().anyMatch(user -> user.getName().equals(username));
    }
}
