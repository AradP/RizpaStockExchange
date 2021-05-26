package resources.controllers;

import bl.StockManager;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import models.Order;
import models.Stock;
import models.Transaction;
import resources.interfaces.OrderActionListener;
import resources.interfaces.TransactionActionsListener;

public class AdminController implements TransactionActionsListener, OrderActionListener {
    @FXML
    private MenuButton symbolsMenuButton;

    private Stock currentStock;

    @FXML
    private TableView<Order> ordersBuyTable;
    @FXML
    private TableColumn<Order, String> dateColumn;
    @FXML
    private TableColumn<Order, String> orderTypeColumn;
    @FXML
    private TableColumn<Order, Integer> quantityColumn;
    @FXML
    private TableColumn<Order, Double> stockPriceColumn;
    @FXML
    private TableColumn<Order, String> creatorNameColumn;

    @FXML
    private TableView<Order> ordersSellTable;

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> symbolColumn;
    @FXML
    private TableColumn<Transaction, String> timeStampColumn;
    @FXML
    private TableColumn<Transaction, Integer> amountOfStocksColumn;
    @FXML
    private TableColumn<Transaction, Double> priceColumn;
    @FXML
    private TableColumn<Transaction, Double> volumeColumn;
    @FXML
    private TableColumn<Transaction, String> sellerNameColumn;
    @FXML
    private TableColumn<Transaction, String> buyerNameColumn;
    @FXML
    private HBox hbLineChart;
    @FXML
    private LineChart lineChart;


    @FXML
    public void initialize() {
        initializeStocksMenu();
        dateColumn = new TableColumn<>("Date");
        orderTypeColumn = new TableColumn<>("Type");
        quantityColumn = new TableColumn<>("Amount");
        stockPriceColumn = new TableColumn<>("Stock Price");
        creatorNameColumn = new TableColumn<>("Creator Name");
        initalizeOrdersColumns();

        symbolColumn = new TableColumn<>("Symbol");
        timeStampColumn = new TableColumn<>("Time stamp");
        amountOfStocksColumn = new TableColumn<>("Amount");
        priceColumn = new TableColumn<>("Price");
        volumeColumn = new TableColumn<>("Volume");
        sellerNameColumn = new TableColumn<>("Seller Name");
        buyerNameColumn = new TableColumn<>("Buyer Name");
        initailizeTransactionsColumns();

        ordersBuyTable.getColumns().setAll(dateColumn, orderTypeColumn, quantityColumn, stockPriceColumn, creatorNameColumn);
        ordersSellTable.getColumns().setAll(dateColumn, orderTypeColumn, quantityColumn, stockPriceColumn, creatorNameColumn);
        transactionsTable.getColumns().setAll(symbolColumn, timeStampColumn, amountOfStocksColumn, priceColumn, volumeColumn, sellerNameColumn, buyerNameColumn);
        ordersBuyTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ordersSellTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        transactionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initLineChart();
    }

    private void initailizeTransactionsColumns() {
        symbolColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getSymbol());
            }
        });
        timeStampColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getTimeStamp());
            }
        });
        amountOfStocksColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Transaction, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getAmountOfStocks());
            }
        });
        priceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, Double>, ObservableValue<Double>>() {

            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Transaction, Double> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getPrice());
            }
        });
        volumeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, Double>, ObservableValue<Double>>() {

            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Transaction, Double> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getVolume());
            }
        });
        sellerNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getSeller().getName());
            }
        });
        buyerNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Transaction, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Transaction, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getBuyer().getName());
            }
        });
    }

    private void initalizeOrdersColumns() {
        dateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getTimestamp());
            }
        });
        orderTypeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getOrderType().toString());
            }
        });
        quantityColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Order, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getCount());
            }
        });
        stockPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Double>, ObservableValue<Double>>() {

            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Order, Double> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getRequestedExchangeRate());
            }
        });
        creatorNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getCreator().getName());
            }
        });
    }

    public void initializeStocksMenu() {
        hbLineChart.setVisible(false);
        symbolsMenuButton.getItems().clear();
        symbolsMenuButton.setText("");

        for (final Stock stock : StockManager.getInstance().getStocks()) {
            MenuItem item = new MenuItem(stock.getSymbol());
            item.setOnAction(e -> {
                symbolsMenuButton.setText(stock.getSymbol());
                currentStock = stock;
                hbLineChart.setVisible(true);
                stockHasChosen();
            });

            symbolsMenuButton.getItems().add(item);
        }

        if (!symbolsMenuButton.getItems().isEmpty() && currentStock == null) {
            symbolsMenuButton.getItems().get(0).fire();
        }
    }

    private void stockHasChosen() {
        lineChart.getData().clear();
        ordersBuyTable.getItems().clear();
        ordersSellTable.getItems().clear();
        transactionsTable.getItems().clear();
        ObservableList<Order> buyItems = (ObservableList<Order>) FXCollections.observableArrayList(StockManager.getInstance().getPendingBuyOrder(currentStock.getSymbol()));
        ObservableList<Order> sellItems = (ObservableList<Order>) FXCollections.observableArrayList(StockManager.getInstance().getPendingSellOrder(currentStock.getSymbol()));
        ObservableList<Transaction> transactions = (ObservableList<Transaction>) FXCollections.observableArrayList(StockManager.getInstance().getTransactionsHistory(currentStock.getSymbol()));
        ordersBuyTable.setItems(buyItems);
        ordersSellTable.setItems(sellItems);
        transactionsTable.setItems(transactions);

        for (Transaction transaction : transactions) {
            XYChart.Series<String, Number> data = new XYChart.Series<String, Number>();
            data.getData().add(new XYChart.Data<>(transaction.getTimeStamp(), transaction.getPrice()));

            lineChart.getData().add(data);
        }
    }

    @Override
    public void transactionWasMade() {
        stockHasChosen();
    }

    @Override
    public void newOrderAdded() {
        stockHasChosen();
    }

    private void initLineChart() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time stamp");
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Stock price");
        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Stock price chart");
        hbLineChart.getChildren().add(lineChart);
    }

    @FXML
    private void toggleLineChart() {
        hbLineChart.setVisible(!hbLineChart.isVisible());
    }

}
