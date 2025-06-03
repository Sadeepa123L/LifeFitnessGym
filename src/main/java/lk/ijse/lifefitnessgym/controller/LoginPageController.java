package lk.ijse.lifefitnessgym.controller;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

    private void NavigateTo(String path) {
        try {
            ancMain.getChildren().clear();
            AnchorPane dashBoard = FXMLLoader.load(getClass().getResource(path));

            dashBoard.prefWidthProperty().bind(ancMain.widthProperty());
            dashBoard.prefHeightProperty().bind(ancMain.heightProperty());

            ancMain.getChildren().add(dashBoard);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Location Not Found").show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtUserName.textProperty().addListener((observable, oldValue, newValue) -> validateFields());
        txtPassWord.textProperty().addListener((observable, oldValue, newValue) -> validateFields());

    }

    private void validateFields() {
       boolean isValidUserName = txtUserName.getText().matches("[a-zA-Z0-9]+");
       boolean isValidPassword = txtPassWord.getText().matches("[a-zA-Z0-9]+");

        if (!isValidUserName)
            txtUserName.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
        if (!isValidPassword)
            txtPassWord.setStyle("-fx-border-color: red; -fx-border-radius: 12px; -fx-background-radius: 12px;");
    }

    public void btnGoDashBoardOnAction(ActionEvent actionEvent) throws IOException {
        String userName = txtUserName.getText();
        String password = txtPassWord.getText();

        if (userName.isEmpty() || password.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please fill all the fields", ButtonType.OK).show();
            return;
        }
        try {
            ResultSet resultSet = CrudUtil.execute("SELECT * FROM user WHERE UserName = ? AND Password = ?", userName, password);
            if (!resultSet.next()) {
                try {
                    ancMain.getChildren().clear();
                    AnchorPane load = FXMLLoader.load(getClass().getResource("/view/DashBoard.fxml"));
                    load.prefWidthProperty().bind(ancMain.widthProperty());
                    load.prefHeightProperty().bind(ancMain.heightProperty());
                    ancMain.getChildren().add(load);

                }catch (Exception e){
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
                }
            }else {
                new Alert(Alert.AlertType.ERROR, "Invalid Username or Password", ButtonType.OK).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong", ButtonType.OK).show();
        }
    }

    public void btnGoSingUpPageOnAction(ActionEvent actionEvent) throws IOException {
        ancMain.getChildren().clear();
        AnchorPane load = FXMLLoader.load(getClass().getResource("/view/SingUpPage.fxml"));
        load.prefWidthProperty().bind(ancMain.widthProperty());
        load.prefHeightProperty().bind(ancMain.heightProperty());
        ancMain.getChildren().add(load);
    }
}

