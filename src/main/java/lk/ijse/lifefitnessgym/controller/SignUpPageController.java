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
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }
        private void validateFields(){
            boolean isValidUserName = txtUserName.getText().matches("[a-zA-Z0-9]+");
            boolean isValidPassword = txtPassword.getText().matches("[a-zA-Z0-9]+");
            boolean isValidEmail = txtEmail.getText().matches("[a-zA-Z0-9]+");
        }

    private void loadNextId() throws SQLException {
        String nextId =signUpPageModel.getNextUserId();
        txtId.setText(nextId);
    }


    public void btnGoLoginPageOnAction(ActionEvent actionEvent) throws IOException {
        String id = txtId.getText();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String email = txtEmail.getText();

        UserDto userDto = new UserDto(
                id,
                userName,
                password,
                email
        );

        try {
            boolean isSaved = signUpPageModel.saveMember(userDto);

            if (isSaved) {
                new Alert(Alert.AlertType.INFORMATION, "User saved successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save User.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save User.").show();
        }

        ancSingUpPage.getChildren().clear();
        AnchorPane load = FXMLLoader.load(getClass().getResource("/view/loginPage.fxml"));
        load.prefWidthProperty().bind(ancSingUpPage.widthProperty());
        load.prefHeightProperty().bind(ancSingUpPage.heightProperty());
        ancSingUpPage.getChildren().add(load);
    }
}
