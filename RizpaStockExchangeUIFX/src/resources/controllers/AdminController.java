package resources.controllers;

import bl.BLManager;
import bl.OrderManager;
import bl.StockManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import models.Order;
import models.Stock;
import models.Transaction;
import resources.interfaces.OrderActionListener;
import resources.interfaces.TransactionActionsListener;

import java.util.Map;

public class AdminController implements TransactionActionsListener, OrderActionListener {
    @FXML
    private MenuButton symbolsMenuButton;

    private Stock currentStock;

    @FXML
    private TableView<Map.Entry<Order, String>> ordersBuyTable;
    @FXML
    private TableColumn<Map.Entry<Order, String>, String> dateColumn;
    @FXML
    private TableColumn<Map.Entry<Order, String>, String> orderTypeColumn;
    @FXML
    private TableColumn<Map.Entry<Order, String>, Integer> quantityColumn;
    @FXML
    private TableColumn<Map.Entry<Order, String>, Integer> stockPriceColumn;
    @FXML
    private TableColumn<Map.Entry<Order, String>, String> creatorNameColumn;

    @FXML
    private TableView<Map.Entry<Transaction, String>> transactionsTable;
    @FXML
    private TableColumn<Map.Entry<Transaction, String>, Integer> sellerNameColumn;
    @FXML
    private TableColumn<Map.Entry<Transaction, String>, String> buyerNameColumn;


    @FXML
    public void initialize() {
        initializeStocksMenu();
        dateColumn = new TableColumn<>("Date");
        orderTypeColumn = new TableColumn<>("Type");
        quantityColumn = new TableColumn<>("Amount");
        stockPriceColumn = new TableColumn<>("Stock Price");
        creatorNameColumn = new TableColumn<>("Creator Name");
        initalizeOrdersColumns();

        sellerNameColumn = new TableColumn<>("Seller Name");
        buyerNameColumn = new TableColumn<>("Buyer Name");
        initailizeTransactionsColumns();
        ordersBuyTable.getColumns().setAll(dateColumn, orderTypeColumn, quantityColumn,stockPriceColumn,creatorNameColumn);
        transactionsTable.getColumns().setAll(sellerNameColumn, buyerNameColumn);
    }

    private void initailizeTransactionsColumns(){

    }

    private void initalizeOrdersColumns() {
/*        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getValue());
            }
        });
        orderTypeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getValue());
            }
        });
        quantityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getValue());
            }
        });
        stockPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getKey().getPrice());
            }
        });
        creatorNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<Order, String>, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getKey().getPrice());
            }
        });*/
    }

    private void initializeStocksMenu() {
        symbolsMenuButton.getItems().clear();
        symbolsMenuButton.setText("");

        for (final Stock stock : StockManager.getInstance().getStocks()) {
            MenuItem item = new MenuItem(stock.getSymbol());
            item.setOnAction(e -> {
                symbolsMenuButton.setText(stock.getSymbol());
                currentStock = stock;
                stockHasChosen();
            });

            symbolsMenuButton.getItems().add(item);
        }
    }

    private void stockHasChosen() {
    }

    @Override
    public void transactionWasMade() {

    }

    @Override
    public void newOrderAdded() {
        ordersBuyTable.getItems().clear();
        //ordersBuyTable.setItems(BLManager.getInstance().getPendingBuyOrders(currentStock.getSymbol()));
    }
}
