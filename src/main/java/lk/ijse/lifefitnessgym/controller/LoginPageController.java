package lk.ijse.lifefitnessgym.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginPageController implements Initializable {
    public AnchorPane ancMain;
    public TextField txtUserName;
    public TextField txtPassWord;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUserName.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        txtPassWord.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
    }

    private boolean validateFields() {
        boolean isValid = true;

        String userName = txtUserName.getText();
        String password = txtPassWord.getText();

        if (userName == null || userName.isEmpty() || !userName.matches("^[\\w.@+-]{3,}$")) {
            txtUserName.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
            isValid = false;
        } else {
            txtUserName.setStyle(null);
        }

        if (password == null || password.isEmpty() || password.length() < 6) {
            txtPassWord.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
            isValid = false;
        } else {
            txtPassWord.setStyle(null);
        }

        return isValid;
    }

    public void btnGoDashBoardOnAction(ActionEvent actionEvent) throws IOException {
        if (!validateFields()) {
            new Alert(Alert.AlertType.WARNING, "Please correct the highlighted fields.", ButtonType.OK).show();
            return;
        }

        String userName = txtUserName.getText();
        String password = txtPassWord.getText();

        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE UserName = ? AND Password = ?", userName, password);

            if (resultSet.next()) {
                // Correct login
                AnchorPane load = FXMLLoader.load(getClass().getResource("/view/DashBoard.fxml"));
                ancMain.getChildren().clear();
                load.prefWidthProperty().bind(ancMain.widthProperty());
                load.prefHeightProperty().bind(ancMain.heightProperty());
                ancMain.getChildren().add(load);
            } else {
                new Alert(Alert.AlertType.ERROR, "Invalid Username or Password.", ButtonType.OK).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong during login.", ButtonType.OK).show();
        }
    }

    public void btnGoSingUpPageOnAction(ActionEvent actionEvent) throws IOException {
        AnchorPane load = FXMLLoader.load(getClass().getResource("/view/SingUpPage.fxml"));
        ancMain.getChildren().clear();
        load.prefWidthProperty().bind(ancMain.widthProperty());
        load.prefHeightProperty().bind(ancMain.heightProperty());
        ancMain.getChildren().add(load);
    }
}
