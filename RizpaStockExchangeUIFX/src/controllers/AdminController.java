package controllers;

import bl.StockManager;
import interfaces.OrderActionListener;
import interfaces.TransactionActionsListener;
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

public class AdminController implements TransactionActionsListener, OrderActionListener {
    @FXML
    private MenuButton symbolsMenuButton;

    private Stock currentStock;

    @FXML
    private TableView<Order> ordersBuyTable;
    @FXML
    private TableColumn<Order, String> dateColumnBuy;
    @FXML
    private TableColumn<Order, String> orderTypeColumnBuy;
    @FXML
    private TableColumn<Order, Integer> quantityColumnBuy;
    @FXML
    private TableColumn<Order, Double> stockPriceColumnBuy;
    @FXML
    private TableColumn<Order, String> creatorNameColumnBuy;
    @FXML
    private TableColumn<Order, String> dateColumnSell;
    @FXML
    private TableColumn<Order, String> orderTypeColumnSell;
    @FXML
    private TableColumn<Order, Integer> quantityColumnSell;
    @FXML
    private TableColumn<Order, Double> stockPriceColumnSell;
    @FXML
    private TableColumn<Order, String> creatorNameColumnSell;

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

        initalizeOrdersColumnsBuy();
        initalizeOrdersColumnsSell();

        symbolColumn = new TableColumn<>("Symbol");
        timeStampColumn = new TableColumn<>("Time stamp");
        amountOfStocksColumn = new TableColumn<>("Amount");
        priceColumn = new TableColumn<>("Price");
        volumeColumn = new TableColumn<>("Volume");
        sellerNameColumn = new TableColumn<>("Seller Name");
        buyerNameColumn = new TableColumn<>("Buyer Name");
        initailizeTransactionsColumns();

        ordersBuyTable.getColumns().setAll(dateColumnBuy, orderTypeColumnBuy, quantityColumnBuy, stockPriceColumnBuy, creatorNameColumnBuy);
        ordersSellTable.getColumns().setAll(dateColumnSell, orderTypeColumnSell, quantityColumnSell, stockPriceColumnSell, creatorNameColumnSell);
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

    private void initalizeOrdersColumnsBuy() {
        dateColumnBuy = new TableColumn<>("Date");
        orderTypeColumnBuy = new TableColumn<>("Type");
        quantityColumnBuy = new TableColumn<>("Amount");
        stockPriceColumnBuy = new TableColumn<>("Stock Price");
        creatorNameColumnBuy = new TableColumn<>("Creator Name");

        dateColumnBuy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getTimestamp());
            }
        });
        orderTypeColumnBuy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getOrderType().toString());
            }
        });
        quantityColumnBuy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Order, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getCount());
            }
        });
        stockPriceColumnBuy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Double>, ObservableValue<Double>>() {

            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Order, Double> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getRequestedExchangeRate());
            }
        });
        creatorNameColumnBuy.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getCreator().getName());
            }
        });
    }

    private void initalizeOrdersColumnsSell() {
        dateColumnSell = new TableColumn<>("Date");
        orderTypeColumnSell = new TableColumn<>("Type");
        quantityColumnSell = new TableColumn<>("Amount");
        stockPriceColumnSell = new TableColumn<>("Stock Price");
        creatorNameColumnSell = new TableColumn<>("Creator Name");

        dateColumnSell.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getTimestamp());
            }
        });
        orderTypeColumnSell.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order, String> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getOrderType().toString());
            }
        });
        quantityColumnSell.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Order, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getCount());
            }
        });
        stockPriceColumnSell.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, Double>, ObservableValue<Double>>() {

            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Order, Double> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper(p.getValue().getRequestedExchangeRate());
            }
        });
        creatorNameColumnSell.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order, String>, ObservableValue<String>>() {

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
