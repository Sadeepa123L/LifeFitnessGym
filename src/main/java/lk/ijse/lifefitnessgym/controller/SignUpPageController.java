package lk.ijse.lifefitnessgym.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import lk.ijse.lifefitnessgym.dto.UserDto;
import lk.ijse.lifefitnessgym.model.SignUpPageModel;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpPageController implements Initializable {
    public AnchorPane ancSingUpPage;
    public TextField txtUserName;
    public TextField txtPassword;
    public TextField txtEmail;
    public Label txtId;

    SignUpPageModel signUpPageModel = new SignUpPageModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUserName.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        txtPassword.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        txtEmail.textProperty().addListener((observable, oldValue, newValue) -> validateFields());

        try {
            loadNextId();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong loading the next ID").show();
        }
    }

    private boolean validateFields() {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String email = txtEmail.getText();

        if (userName == null || userName.isEmpty() || !userName.matches("^[a-zA-Z0-9]{4,20}$")) {
            return false;
        }

        if (password == null || password.isEmpty() || password.length() < 6) {
            return false;
        }

        if (email == null || email.isEmpty() || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return false;
        }

        return true;
    }

    private void loadNextId() throws SQLException {
        String nextId = signUpPageModel.getNextUserId();
        txtId.setText(nextId);
    }

    public void btnGoLoginPageOnAction(ActionEvent actionEvent) throws IOException {
        if (!validateFields()) {
            new Alert(Alert.AlertType.WARNING, "Please enter valid Username, Password (min 6 chars), and Email.").show();
            return;
        }

        String id = txtId.getText();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String email = txtEmail.getText();

        UserDto userDto = new UserDto(id, userName, password, email);

        try {
            boolean isSaved = signUpPageModel.saveMember(userDto);
            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "User saved successfully.").show();
                loadLoginPage();
            } else {
                new Alert(Alert.AlertType.ERROR, "Failed to save user.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "An error occurred while saving the user.").show();
        }
    }

    private void loadLoginPage() throws IOException {
        ancSingUpPage.getChildren().clear();
        AnchorPane load = FXMLLoader.load(getClass().getResource("/view/loginPage.fxml"));
        load.prefWidthProperty().bind(ancSingUpPage.widthProperty());
        load.prefHeightProperty().bind(ancSingUpPage.heightProperty());
        ancSingUpPage.getChildren().add(load);
    }
}
