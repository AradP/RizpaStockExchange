package bl;

import enums.Role;
import exceptions.users.UserAlreadyExistsException;
import models.User;

import java.util.List;

public class UsersSessionManager {
    // current logged in users
    private static List<User> users;

    public static User login(String username, Role role) throws UserAlreadyExistsException {
        if (!isLoggedIn(username)) {
            User user = new User(username, role);
            users.add(user);

            return user;
        }

        return null;
    }

    private static boolean isLoggedIn(String username) {
        return users.stream().map(user -> user.getName().equals(username)).findAny().get();
    }
}
