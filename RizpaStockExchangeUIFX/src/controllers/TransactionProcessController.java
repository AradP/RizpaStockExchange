package controllers;

import bl.UserManager;
import interfaces.OrderActionListener;
import interfaces.TransactionActionsListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import models.Stock;
import models.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionProcessController implements TransactionActionsListener, OrderActionListener {
    @FXML
    private MenuButton usersMenuButton;
    @FXML
    private Label userNameLabel;
    @FXML
    private TableView<Map.Entry<Stock, Integer>> stocksTable;
    @FXML
    private TableColumn<Map.Entry<Stock, Integer>, String> symbolColumn;
    @FXML
    private TableColumn<Map.Entry<Stock, Integer>, Integer> quantityColumn;
    @FXML
    private TableColumn<Map.Entry<Stock, Integer>, Integer> stockPriceColumn;
    @FXML
    private Label valueOfAllStocksLabel;
    @FXML
    private ScrollPane spTransactionCore;

    private User currentUser;
    private TransactionProcessCoreController coreController;
    private List<TransactionActionsListener> transactionActionsListeners;
    private List<OrderActionListener> orderActionsListeners;

    @FXML
    public void initialize() {
        transactionActionsListeners = new ArrayList<TransactionActionsListener>();
        orderActionsListeners = new ArrayList<OrderActionListener>();
        symbolColumn = new TableColumn<>("Symbol");
        quantityColumn = new TableColumn<>("Quantity");
        stockPriceColumn = new TableColumn<>("Stock price");
        initalizeColumns();
        stocksTable.getColumns().setAll(symbolColumn, quantityColumn, stockPriceColumn);
        initializeTransactionProcessCore();
    }

    public void addTransactionActionsListener(TransactionActionsListener listener) {
        transactionActionsListeners.add(listener);
    }

    public void removeTransactionActionsListener(TransactionActionsListener listener) {
        transactionActionsListeners.remove(listener);
    }

    private void initializeTransactionProcessCore() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxmls/TransactionProcessCoreComponent.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException ioException) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There was a problem loading te fxml file: " + ioException.getMessage());
            alert.show();
        }
        coreController = loader.getController();
        coreController.addTransactionActionsListener(this);
        coreController.addOrderActionsListener(this);
        spTransactionCore.setContent(node);
        spTransactionCore.setVisible(false);
    }

    public void clearUserMenu() {
        usersMenuButton.getItems().clear();
        stocksTable.getItems().clear();
    }

    private void initalizeColumns() {
        symbolColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, String> p) {
                // this callback returns property for just one cell, you can't use a loop here
                // for first column we use key
                return new SimpleStringProperty(p.getValue().getKey().getSymbol());
            }
        });
        quantityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper<>(p.getValue().getValue());
            }
        });
        stockPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getKey().getPrice());
            }
        });
    }

    public void refreshUsers() {
        clearUserMenu();
        for (final User user : UserManager.getInstance().getUsers()) {
            MenuItem item = new MenuItem(user.getName());
            item.setOnAction(e -> {
                usersMenuButton.setText(user.getName());
                currentUser = user;
                // Fill the stock data
                userNameLabel.setText(user.getName());
                refreshStocksTable(user);
                refreshValueOfAllStocks(user);
                coreController.setCurrentUser(currentUser);
            });
            usersMenuButton.getItems().add(item);
        }
    }

    private void refreshStocksTable(User user) {
        ObservableList<Map.Entry<Stock, Integer>> items = (ObservableList<Map.Entry<Stock, Integer>>) FXCollections.observableArrayList(user.getHoldings().entrySet());
        stocksTable.setItems(items);
    }

    private void refreshValueOfAllStocks(User user) {
        int sum = 0;
        for (Map.Entry<Stock, Integer> entry : user.getHoldings().entrySet()) {
            sum += entry.getValue() * entry.getKey().getPrice();
        }
        valueOfAllStocksLabel.setText("Value of the portfolio: " + String.valueOf(sum));
    }

    @FXML
    public void createTransaction(ActionEvent actionEvent) {
        spTransactionCore.setVisible(true);
    }


    @Override
    public void transactionWasMade() {
        refreshUsers();
        for (MenuItem item : usersMenuButton.getItems()) {
            if(item.getText() == currentUser.getName()){
                item.fire();
            }
        }
        for (TransactionActionsListener listener : transactionActionsListeners) {
            listener.transactionWasMade();
        }
    }

    @Override
    public void newOrderAdded() {
        for (OrderActionListener listener : orderActionsListeners) {
            listener.newOrderAdded();
        }
    }
}
