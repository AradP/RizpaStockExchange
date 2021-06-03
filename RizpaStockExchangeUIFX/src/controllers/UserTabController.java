package controllers;

import interfaces.TransactionActionsListener;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import models.Stock;
import models.User;

import java.util.Map;

public class UserTabController implements TransactionActionsListener {
    @FXML
    private Label userMainLabel;

    @FXML
    private User user;

    @FXML
    private TableView<Map.Entry<Stock, Integer>> stocksTable;

    @FXML
    private TableColumn<Map.Entry<Stock, Integer>, String> symbol;

    @FXML
    private TableColumn<Map.Entry<String, Integer>, Integer> quantity;

    @FXML
    public void initialize() {
        // use fully detailed type for Map.Entry<String, String>
        TableColumn<Map.Entry<Stock, Integer>, String> column1 = new TableColumn<>("Stock Symbol");
        column1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, String> p) {
                // this callback returns property for just one cell, you can't use a loop here
                // for first column we use key
                return new SimpleStringProperty(p.getValue().getKey().getSymbol());
            }
        });

        TableColumn<Map.Entry<Stock, Integer>, Integer> column2 = new TableColumn<>("Quantity");
        column2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, Integer>, ObservableValue<Integer>>() {

            @Override
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Map.Entry<Stock, Integer>, Integer> p) {
                // for second column we use value
                return new ReadOnlyObjectWrapper<>(p.getValue().getValue());
            }
        });
        stocksTable.getColumns().setAll(column1, column2);
    }

    public void setUser(final User user) {
        this.user = user;
        userMainLabel.setText(user.getName());

        refreshStocksTable();
    }

    private void refreshStocksTable() {
        ObservableList<Map.Entry<Stock, Integer>> items = (ObservableList<Map.Entry<Stock, Integer>>) FXCollections.observableArrayList(user.getHoldings().entrySet());
        stocksTable.getItems().clear();
        stocksTable.setItems(items);


    }

    @Override
    public void transactionWasMade() {
        refreshStocksTable();
    }
}
