package bl;

public final class OrderManager {

    //region Lazy Singleton

    private static OrderManager instance;

    private OrderManager() {
    }

    public static OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    //endregion

}
