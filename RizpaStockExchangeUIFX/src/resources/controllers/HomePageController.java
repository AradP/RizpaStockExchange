package resources.controllers;

import bl.BLManager;
import bl.StockManager;
import bl.UserManager;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import models.Stock;
import models.User;
import resources.interfaces.TransactionActionsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomePageController implements TransactionActionsListener {
    @FXML
    private Label xmlUploadProgressLabel;

    @FXML
    private ProgressBar xmlLoadProgressBar;

    @FXML
    private BorderPane rootBorderPane;

    @FXML
    private TabPane rootTabPane;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Stock> stocksTable;

    @FXML
    private MenuButton stocksMenuButton;

    @FXML
    private HBox singleStockInfoBox;

    @FXML
    private TableColumn<Stock, String> symbol;
    @FXML
    private TableColumn<Stock, String> companyName;
    @FXML
    private TableColumn<Stock, Integer> stockPrice;
    @FXML
    private TableColumn<Stock, Integer> totalTransacsNum;
    @FXML
    private TableColumn<Stock, Double> totalTransacsVol;

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
    private CheckBox turnAnimationsOnCheckbox;

    @FXML
    private Tab transactionProcessTab;
    @FXML
    private Tab adminTab;

    private TransactionProcessController transactionProcessController;
    private AdminController adminController;

    private boolean areAnimationsOn = false;

    @FXML
    public void initialize() {
        initializeTransactionProcessTab();
        initializeAdminTab();
    }

    private void initializeAdminTab() {
        adminTab = new Tab("Admin");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxmls/AdminTab.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException ioException) {
            // TODO: Alert
            ioException.printStackTrace();
        }

        adminController = loader.getController();
        transactionProcessController.addTransactionActionsListener(adminController);
        adminTab.setContent(node);
        rootTabPane.getTabs().add(adminTab);
    }

    @FXML
    private void setAnimationsOn() {
        final Timeline timeline = new Timeline();

        if (turnAnimationsOnCheckbox.isSelected()) {
            areAnimationsOn = true;

            timeline.setCycleCount(Timeline.INDEFINITE);
            final KeyValue kv = new KeyValue(welcomeLabel.translateXProperty(), 300);
            final KeyFrame kf = new KeyFrame(Duration.millis(1000), kv);
            timeline.getKeyFrames().add(kf);
            timeline.setAutoReverse(true);
            timeline.setCycleCount(2);
            timeline.play();
        } else {
            areAnimationsOn = false;
            timeline.stop();
        }
    }

    @FXML
    private void loadSystemDataFromXMLFile(ActionEvent event) {
        event.consume();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        File selectedFile = fileChooser.showOpenDialog(rootBorderPane.getScene().getWindow());

        if (selectedFile != null) {
            xmlLoadProgressBar.setVisible(true);
            xmlUploadProgressLabel.setVisible(true);

            startXmlFileUploadTask(selectedFile.getAbsolutePath(), event);
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
        stocksMenuButton.getItems().clear();

        for (final Stock stock : StockManager.getInstance().getStocks()) {
            MenuItem item = new MenuItem(stock.getSymbol());
            item.setOnAction(e -> {
                stocksMenuButton.setText(stock.getSymbol());

                // Fill the stock data
                singleStockSymbol.setText(stock.getSymbol());
                singleStockCompanyName.setText(stock.getCompanyName());
                singleStockPrice.setText(String.valueOf(stock.getPrice()));
                singleStockTotalTransNum.setText(String.valueOf(stock.getCompletedTransactions().size()));
                singleStockTotalTransVol.setText(String.valueOf(stock.getOrderPeriod()));

                if (areAnimationsOn) {
                    FadeTransition ft = new FadeTransition(Duration.millis(3000), singleStockInfoBox);
                    ft.setFromValue(0.1);
                    ft.setToValue(1.0);
                    ft.play();
                }
            });

            stocksMenuButton.getItems().add(item);
        }

    }

    private void refreshUsersTabs() {
        for (final User user : UserManager.getInstance().getUsers()) {
            Tab currTab = new Tab(user.getName());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxmls/UserTab.fxml"));
            Node node = null;
            try {
                node = loader.load();
            } catch (IOException ioException) {
                // TODO: Alert
                ioException.printStackTrace();
            }
            // Get the Controller from the FXMLLoader
            UserTabController controller = loader.getController();
            // Set data in the controller
            controller.setUser(user);
            transactionProcessController.addTransactionActionsListener(controller);
            currTab.setContent(node);
            rootTabPane.getTabs().add(currTab);
        }
    }

    private void initializeTransactionProcessTab() {
        transactionProcessTab = new Tab("Transaction Process");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxmls/TransactionProcessTab.fxml"));
        Node node = null;
        try {
            node = loader.load();
        } catch (IOException ioException) {
            // TODO: Alert
            ioException.printStackTrace();
        }

        transactionProcessController = loader.getController();
        transactionProcessController.addTransactionActionsListener(this);
        transactionProcessTab.setContent(node);
        rootTabPane.getTabs().add(transactionProcessTab);
    }

    private void refreshTransactionProcessTab() {
        transactionProcessController.refreshUsers();
    }

    @FXML
    private void exitSystem(ActionEvent event) {
        event.consume();

        System.exit(0);
    }

    private void startXmlFileUploadTask(final String path, final ActionEvent event) {
        // Create a background Task
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // Listen to the progress change
                BLManager.getInstance().progressProperty().addListener((obs, oldProgress, newProgress) -> {
                    updateProgress(newProgress.doubleValue(), 100);
                    updateMessage(String.valueOf(newProgress.doubleValue()));
                });

                // Load the xml file
                BLManager.getInstance().loadConfigurationFileByPath(path);

                return null;
            }
        };

        // This method allows us to handle any Exceptions thrown by the task
        task.setOnSucceeded(wse -> {
            refreshStocksTable();
            refreshStocksMenuTable();
            refreshTransactionProcessTab();
            refreshUsersTabs();
            adminController.initializeStocksMenu();

            EventHandler<ActionEvent> alertEvent = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    // show the dialog
                    new Alert(Alert.AlertType.CONFIRMATION,
                            "Successfully updated the stocks in the system", ButtonType.CLOSE).show();
                }
            };
            alertEvent.handle(event);

            xmlLoadProgressBar.setVisible(false);
            xmlUploadProgressLabel.setVisible(false);
        });

        task.setOnFailed(wse -> {
            EventHandler<ActionEvent> alertEvent = new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    // show the dialog
                    new Alert(Alert.AlertType.ERROR,
                            task.getException().getMessage(), ButtonType.CLOSE).show();
                }
            };
            alertEvent.handle(event);
            xmlLoadProgressBar.setVisible(false);
            xmlUploadProgressLabel.setVisible(false);
        });

        // Before starting our task, we need to bind our UI values to the properties on the task
        xmlLoadProgressBar.progressProperty().bind(task.progressProperty());
        xmlUploadProgressLabel.textProperty().bind(task.messageProperty());

        // Now, start the task on a background thread
        new Thread(task).start();
    }

    @Override
    public void transactionWasMade() {

    }
//
//    @FXML
//    private void changeTheme(ActionEvent event, int themeNum, Stage primaryStage) {
//        Scene scene = ((Node)event.getSource()).getScene();
//        switch (themeNum) {
//            case (1): {
//                scene.getStylesheets().clear();
//                scene.getStylesheets().add(this.getClass().getResource("../css/theme1.css").toExternalForm());
//            } case (2): {
//                scene.getStylesheets().clear();
//                scene.getStylesheets().add(this.getClass().getResource("../css/theme2.css").toExternalForm());
//            } default: {
//                scene.getStylesheets().clear();
//                scene.getStylesheets().add(this.getClass().getResource("../css/basic.css").toExternalForm());
//            }
//        }
//    }
//
//    @FXML
//    public void chThemeToDefault(ActionEvent event) {
//        changeTheme(event, 0);
//    }
//
//    @FXML
//    public void chThemeTo1(ActionEvent event) {
//        changeTheme(event, 1);
//    }
//
//    @FXML
//    public void chThemeTo2(ActionEvent event) {
//        changeTheme(event, 2);
//    }
}
