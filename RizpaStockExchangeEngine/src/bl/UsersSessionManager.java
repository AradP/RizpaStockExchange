package bl;

import enums.Role;
import enums.TradeActivityType;
import exceptions.users.UserAlreadyExistsException;
import models.TradeActivity;
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

    public ArrayList<TradeActivity> getTradeActivities(User currentUser) {
        User specUser = users.stream().filter(user -> user.getName().equalsIgnoreCase(currentUser.getName())).findFirst().get();
        return specUser.getTradeActivities();
    }

    /**
     * @param currentUser
     * @param moneyToAdd
     */
    public User addMoneyToSpecUser(User currentUser, int moneyToAdd) {
        User specUser = users.stream().filter(user -> user.getName().equalsIgnoreCase(currentUser.getName())).findFirst().get();
        specUser.addMoney(moneyToAdd);
        return specUser;
    }

    public User AddStockToUserByIPO(User currentUser, String symbol, int amount) {
        User specUser = users.stream().filter(user -> user.getName().equalsIgnoreCase(currentUser.getName())).findFirst().get();
        specUser.addStocks(StockManager.getInstance().getStockBySymbol(symbol), amount);
        specUser.getTradeActivities().add(new TradeActivity(TradeActivityType.BUY_STOCK, symbol, 0, specUser.getCurrentMoney()));
        return specUser;
    }

    public User getUserByName(String name) {
        User specUser = users.stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst().get();
        return specUser;
    }

    private boolean isLoggedIn(String username) {
        return users.stream().anyMatch(user -> user.getName().equals(username));
    }


}
