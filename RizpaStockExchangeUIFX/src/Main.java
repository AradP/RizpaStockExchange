import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        FXMLLoader loader = new FXMLLoader();
        BorderPane borderPane = null;
        Scene scene = null;

        try {
            loader.setLocation(getClass().getResource("resources/fxmls/HomePage.fxml"));
            borderPane = loader.<BorderPane>load();
            scene = new Scene(borderPane);

        } catch (final IOException e) {
            System.out.println("TODO: " + e.getMessage());
            scene = new Scene(new VBox());
        }

        scene.getStylesheets().add(this.getClass().getResource("resources/css/basic.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}