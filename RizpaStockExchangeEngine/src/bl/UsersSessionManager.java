package bl;

import exceptions.users.UserAlreadyExistsException;
import models.User;

import java.util.List;

public class UsersSessionManager {
    // current logged in users
    private static List<User> users;

    public static void login (String username) throws UserAlreadyExistsException {
        if (!isLoggedIn(username)) {
            users.add(new User(username));
        }
    }

    private static boolean isLoggedIn(String username) {
        return users.stream().map(user -> user.getName().equals(username)).findAny().get();
    }
}
