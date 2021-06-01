import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private final String theme1Url = getClass().getResource("resources/css/theme1.css").toExternalForm();
    private final String theme2Url = getClass().getResource("resources/css/theme2.css").toExternalForm();
    private final String basicTheme = getClass().getResource("resources/css/basic.css").toExternalForm();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader();
        BorderPane borderPane = null;

        try {
            loader.setLocation(getClass().getResource("resources/fxmls/HomePage.fxml"));
            borderPane = loader.load();
        } catch (final IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "There was a problem loading te fxml file: " + e.getMessage());
            alert.show();
        }

        final Scene scene;
        if (borderPane != null) {
            scene = new Scene(borderPane);

            scene.getStylesheets().add(theme1Url);
            scene.getStylesheets().clear();

            final Button btn = new Button("Basic Theme");
            btn.getStyleClass().add("buttonStyle");
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(basicTheme);
                }
            });

            final Button btn2 = new Button("Theme 1");
            btn2.getStyleClass().add("buttonStyle");
            btn2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(theme1Url);
                }
            });

            final Button btn3 = new Button("Theme 2");
            btn3.getStyleClass().add("buttonStyle");
            btn3.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    scene.getStylesheets().clear();
                    scene.getStylesheets().add(theme2Url);
                }
            });

            borderPane.setRight(VBoxBuilder.create().spacing(10).children(btn, btn2, btn3).build());

            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}