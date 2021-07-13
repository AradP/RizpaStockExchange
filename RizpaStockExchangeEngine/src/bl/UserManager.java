package bl;

import enums.TradeActivityType;
import models.TradeActivity;
import models.User;

import java.util.ArrayList;

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
}
