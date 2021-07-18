package bl;

import enums.TradeActivityType;
import models.TradeActivity;
import models.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/*
Adding and retrieving users is synchronized and in that manner - these actions are thread safe
Note that asking if a user exists (isUserExists) does not participate in the synchronization and it is the responsibility
of the user of this class to handle the synchronization of isUserExists with other methods here on it's own
 */
public class UserManager {

    private final Set<User> usersSet;

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(User username) {
        usersSet.add(username);
    }

    public synchronized void removeUser(User user) {
        usersSet.remove(user);
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public synchronized boolean isUserExists(User user) {
        return usersSet.contains(user);
    }

    public synchronized boolean isLoggedIn(String username) {
        return usersSet.stream().anyMatch(user -> user.getName().equals(username));
    }

    public ArrayList<TradeActivity> getTradeActivities(User currentUser) {
        User specUser = getUsers().stream().filter(user -> user.getName().equalsIgnoreCase(currentUser.getName())).findFirst().get();
        return specUser.getTradeActivities();
    }

    /**
     * @param currentUser
     * @param moneyToAdd
     */
    public User addMoneyToSpecUser(User currentUser, int moneyToAdd) {
        User specUser = getUsers().stream().filter(user -> user.getName().equalsIgnoreCase(currentUser.getName())).findFirst().get();
        specUser.addMoney(moneyToAdd);
        return specUser;
    }

    public User AddStockToUserByIPO(User currentUser, String symbol, int amount) {
        User specUser = getUsers().stream().filter(user -> user.getName().equalsIgnoreCase(currentUser.getName())).findFirst().get();
        specUser.addStocks(StockManager.getInstance().getStockBySymbol(symbol), amount);
        specUser.getTradeActivities().add(new TradeActivity(TradeActivityType.BUY_STOCK, symbol, 0, specUser.getCurrentMoney()));
        return specUser;
    }

    public User getUserByName(String name) {
        User specUser = getUsers().stream().filter(user -> user.getName().equalsIgnoreCase(name)).findFirst().get();
        return specUser;
    }
}
