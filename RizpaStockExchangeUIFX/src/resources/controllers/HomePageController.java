package resources.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class HomePageController {
    @FXML
    private void loadSystemDataFromXMLFile(ActionEvent event) {
        event.consume();

//        try {
//            APIGatewayManager.getInstance().loadConfigurationFileByPath(xmlFilePathTextField.getText());
        System.out.println("Successfully updated the stocks in the system");
//        } catch (StockException | FileNotFoundException | JAXBException e) {
//            System.out.println(e.getMessage());
//        }
    }
}
