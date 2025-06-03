package lk.ijse.lifefitnessgym.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    public AnchorPane ancMain;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        NavigateTo("/view/memberPage.fxml");
    }

    public void btnGoTrainerPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/TrainerPage.fxml");
    }
    public void btnGoMemberPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/MemberPage.fxml");
    }
    public void btnGoWorkoutSessionPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/WorkoutSessionPage.fxml");
    }
    public void btnGoDietPlanPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/DietPlanPage.fxml");
    }
    public void btnGoDietPlanOrderPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/DietPlanOrderPage.fxml");
    }
    public void btnGoSupplementPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/SupplementPage.fxml");
    }
    public void btnGoMembershipPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/MembershipPage.fxml");
    }
    public void btnGoCashierPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/CashierPage.fxml");
    }

    public void btnGoSupplementOrderPageOnAction(ActionEvent actionEvent) {NavigateTo("/view/SupplementOrderPage.fxml");}

    public void btnGoReservationOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/ReservationPage.fxml");
    }
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

    public void btnGoDetailsPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/SupplementOrderDetailsPage.fxml");
    }

    public void btnGoSubscriptionPageOnAction(ActionEvent actionEvent) {
        NavigateTo("/view/SubscriptionPage.fxml");
    }
}
