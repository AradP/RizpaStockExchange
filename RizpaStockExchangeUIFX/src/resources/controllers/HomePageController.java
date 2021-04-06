package resources.controllers;

import apigateway.APIGatewayManager;
import bl.StockManager;
import exceptions.StockException;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import models.Stock;
import models.Transaction;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HomePageController {
    @FXML
    private TableView<Stock> stocksTable;

    @FXML
    private MenuButton stocksMenuButton;

    @FXML
    public TableColumn<Stock, String> symbol;
    @FXML
    public TableColumn<Stock, String> companyName;
    @FXML
    public TableColumn<Stock, Integer> stockPrice;
    @FXML
    public TableColumn<Stock, Integer> totalTransacsNum;
    @FXML
    public TableColumn<Stock, Double> totalTransacsVol;
    @FXML
    private TextField xmlFilePathTextField;

    @FXML
    private Label singleStockSymbol;
    @FXML
    private Label singleStockCompanyName;
    @FXML
    private Label singleStockPrice;
    @FXML
    private Label singleStockTotalTransNum;
    @FXML
    private Label singleStockTotalTransVol;

    @FXML
    private void loadSystemDataFromXMLFile(ActionEvent event) {
        event.consume();

        try {
            APIGatewayManager.getInstance().loadConfigurationFileByPath(xmlFilePathTextField.getText());

            refreshStocksTable();
            refreshStocksMenuTable();
            // TODO: alert instead
            System.out.println("Successfully updated the stocks in the system");
        } catch (StockException | FileNotFoundException | JAXBException e) {
            System.out.println(e.getMessage());
        }
    }

    private void refreshStocksTable() {
        symbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        companyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        stockPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        totalTransacsNum.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Stock, Integer>, ObservableValue<Integer>>() {
            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Stock, Integer> param) {
                return new ReadOnlyObjectWrapper(param.getValue().getCompletedTransactions().size());
            }
        });
        totalTransacsVol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Stock, Double>, ObservableValue<Double>>() {
            @Override
            public ObservableValue<Double> call(TableColumn.CellDataFeatures<Stock, Double> param) {
                return new ReadOnlyObjectWrapper<Double>(param.getValue().getOrderPeriod());
            }
        });
        stocksTable.getItems().setAll(StockManager.getInstance().getStocks());
    }

    private void refreshStocksMenuTable() {
        for (final Stock stock : StockManager.getInstance().getStocks()) {
            MenuItem item = new MenuItem(stock.getSymbol());
            item.setOnAction(e -> {
                // Fill the stock data
                singleStockSymbol.setText(stock.getSymbol());
                singleStockCompanyName.setText(stock.getCompanyName());
                singleStockPrice.setText(String.valueOf(stock.getPrice()));
                singleStockTotalTransNum.setText(String.valueOf(stock.getCompletedTransactions().size()));
                singleStockTotalTransVol.setText(String.valueOf(stock.getOrderPeriod()));
            });

            stocksMenuButton.getItems().add(item);
        }

    }

    @FXML
    private void showSingleStock(ActionEvent event) {
        event.consume();

        System.exit(0);
    }

    @FXML
    private void exitSystem(ActionEvent event) {
        event.consume();

        System.exit(0);
    }
}
