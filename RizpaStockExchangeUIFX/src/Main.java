import apigateway.APIGatewayManager;
import exceptions.StockException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        primaryStage.setTitle("Rizpa Stock Exchange");

        addXMLFileLoader(grid);

        primaryStage.setScene(new Scene(grid, 300, 275));
        primaryStage.show();
    }

    private void addXMLFileLoader(final GridPane grid) {
        // Load XML file path field
        Label xmlFilePath = new Label("XML File Path:");
        grid.add(xmlFilePath, 0, 1);

        TextField xmlFilePathTextField = new TextField();
        grid.add(xmlFilePathTextField, 1, 1);

        // Load XML file button
        Button btnLoadXml = new Button();
        btnLoadXml.setText("Load XML file");
        btnLoadXml.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    APIGatewayManager.getInstance().loadConfigurationFileByPath(xmlFilePathTextField.getText());
                    System.out.println("Successfully updated the stocks in the system");
                } catch (StockException | FileNotFoundException | JAXBException e) {
                    System.out.println(e.getMessage());
                }

            }
        });

        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(btnLoadXml);

        grid.add(hbBtn, 1, 4);
    }
}