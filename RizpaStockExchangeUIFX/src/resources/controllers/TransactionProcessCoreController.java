package resources.controllers;

import apigateway.APIGatewayManager;
import bl.StockManager;
import exceptions.stocks.StockException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.Order;
import models.Stock;
import models.User;
import resources.interfaces.OrderActionListener;
import resources.interfaces.TransactionActionsListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransactionProcessCoreController implements OrderActionListener {

    @FXML
    private RadioButton rbSell;
    @FXML
    private RadioButton rbBuy;
    @FXML
    private ToggleGroup toggleGroupSaleBuy;
    @FXML
    private RadioButton rbMKT;
    @FXML
    private RadioButton rbLMT;
    @FXML
    private ToggleGroup toggleGroupMktLmt;
    @FXML
    private MenuButton symbolsMenuButton;
    @FXML
    private HBox hbLimitRate;
    @FXML
    private TextField tfLimitRate;
    @FXML
    private TextField tfAmountOfStocks;
    @FXML
    private CheckBox cbConfirm;
    @FXML
    private Button btConfirm;

    private boolean isSell;
    private boolean isLMT;

    private User currentUser;
    private Stock currentStock;
    private int stockAmountLimit;
    private List<TransactionActionsListener> transactionActionsListeners;
    private List<OrderActionListener> orderActionsListeners;

    @FXML
    public void initialize() {
        transactionActionsListeners = new ArrayList<TransactionActionsListener>();
        orderActionsListeners = new ArrayList<>();
        toggleGroupSaleBuy = new ToggleGroup();
        rbSell.setToggleGroup(toggleGroupSaleBuy);
        rbBuy.setToggleGroup(toggleGroupSaleBuy);
        rbSell.setSelected(true);
        isSell = true;

        toggleGroupMktLmt = new ToggleGroup();
        rbMKT.setToggleGroup(toggleGroupMktLmt);
        rbLMT.setToggleGroup(toggleGroupMktLmt);
        rbLMT.setSelected(true);
        isLMT = true;


        initializeAll();
    }

    public void addTransactionActionsListener(TransactionActionsListener listener) {
        transactionActionsListeners.add(listener);
    }

    public void removeTransactionActionsListener(TransactionActionsListener listener) {
        transactionActionsListeners.remove(listener);
    }
    public void removeOrderActionsListener(OrderActionListener listener) {
        orderActionsListeners.remove(listener);
    }

    public void addOrderActionsListener(OrderActionListener listener){
        orderActionsListeners.add(listener);
    }

    private void initializeAll() {
        initializeRbSell();
        initializeRbLmt();
        initializeTextFieldLimitRate();
        initializeTextFieldAmountOfStocks();
        initializeCheckBoxConfirm();
    }

    private void initializeCheckBoxConfirm() {
        cbConfirm.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                                Boolean old_val, Boolean new_val) {
                btConfirm.setDisable(!new_val);
            }
        });
    }

    private void initializeTextFieldLimitRate() {
        tfLimitRate.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    tfLimitRate.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private void initializeTextFieldAmountOfStocks() {
        tfAmountOfStocks.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.isEmpty()) {
                    if (!newValue.matches("\\d*")) {
                        tfAmountOfStocks.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                    if (isSell && Integer.parseInt(newValue) > stockAmountLimit) {
                        tfAmountOfStocks.setText(oldValue);
                    }
                }
            }
        });
    }

    private void initializeRbSell() {
        rbSell.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                isSell = !isSell;
                initializeStocksMenu();
                tfLimitRate.setText("");
            }
        });
    }

    private void initializeRbLmt() {
        rbLMT.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                isLMT = !isLMT;
                hbLimitRate.setVisible(isLMT);
                tfLimitRate.setText("");
            }
        });
    }

    private void stockHasChosen() {
        tfAmountOfStocks.setDisable(false);
        tfAmountOfStocks.setEditable(true);
        tfLimitRate.setText("");
        tfAmountOfStocks.setText("");
    }

    private void initializeStocksMenu() {
        symbolsMenuButton.getItems().clear();
        symbolsMenuButton.setText("");
        tfAmountOfStocks.setText("");
        tfAmountOfStocks.setDisable(true);
        tfAmountOfStocks.setEditable(false);
        btConfirm.setDisable(true);
        cbConfirm.setSelected(false);

        if (!isSell) {
            for (final Stock stock : StockManager.getInstance().getStocks()) {
                MenuItem item = new MenuItem(stock.getSymbol());
                item.setOnAction(e -> {
                    symbolsMenuButton.setText(stock.getSymbol());
                    currentStock = stock;
                    stockHasChosen();
                });

                symbolsMenuButton.getItems().add(item);
            }
        } else {
            for (Map.Entry<Stock, Integer> entry : currentUser.getHoldings().entrySet()) {
                MenuItem item = new MenuItem(entry.getKey().getSymbol());
                item.setOnAction(e -> {
                    symbolsMenuButton.setText(entry.getKey().getSymbol());
                    currentStock = entry.getKey();
                    stockAmountLimit = entry.getValue();
                    stockHasChosen();
                });

                symbolsMenuButton.getItems().add(item);
            }
        }
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        initializeStocksMenu();
    }

    private void refreshDetails() {
        initializeStocksMenu();
        for (TransactionActionsListener listener : transactionActionsListeners) {
            listener.transactionWasMade();
        }
    }

    @FXML
    public void confirmTransaction(ActionEvent actionEvent) {
        try {
            // LMT
            if (isLMT) {
                String returnValue = isSell ? APIGatewayManager.getInstance().sellLMTOrder(currentStock.getSymbol(), Integer.parseInt(tfAmountOfStocks.getText()), Integer.parseInt(tfLimitRate.getText()), currentUser) :
                        APIGatewayManager.getInstance().buyLMTOrder(currentStock.getSymbol(), Integer.parseInt(tfAmountOfStocks.getText()), Integer.parseInt(tfLimitRate.getText()), currentUser);
                new Alert(Alert.AlertType.CONFIRMATION,
                        returnValue, ButtonType.CLOSE).show();
                refreshDetails();
            }
            //MKT
            else {
                String returnValue = !isSell ? APIGatewayManager.getInstance().sellMKTOrder(currentStock.getSymbol(), Integer.parseInt(tfAmountOfStocks.getText()), currentUser) :
                        APIGatewayManager.getInstance().buyMKTOrder(currentStock.getSymbol(), Integer.parseInt(tfAmountOfStocks.getText()), currentUser);
                new Alert(Alert.AlertType.CONFIRMATION,
                        returnValue, ButtonType.CLOSE).show();
                refreshDetails();
            }
        } catch (StockException e) {
            new Alert(Alert.AlertType.ERROR,
                    e.getMessage(), ButtonType.CLOSE).show();
        }
    }

    @Override
    public void newOrderAdded() {
        for (OrderActionListener listener:orderActionsListeners) {
            listener.newOrderAdded();
        }
    }
}
