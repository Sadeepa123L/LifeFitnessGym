package lk.ijse.lifefitnessgym.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class StartpageController implements Initializable {

    public AnchorPane ancMain;

    private void NavigateTo(String path) {
        try {
            ancMain.getChildren().clear();
            AnchorPane dashBoard = FXMLLoader.load(getClass().getResource(path));

            dashBoard.prefWidthProperty().bind(ancMain.widthProperty());
            dashBoard.prefHeightProperty().bind(ancMain.heightProperty());

            ancMain.getChildren().add(dashBoard);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"Location Not Found").show();
            e.printStackTrace();
        }
    }

    public void btnGoLoginPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/LoginPage.fxml");
    }

    public void btnGoSignUpPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/SignUpPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
