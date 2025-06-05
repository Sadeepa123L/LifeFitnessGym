package lk.ijse.lifefitnessgym;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Appinitializer extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primarystage) throws Exception {
        Parent parent = FXMLLoader.load(getClass().getResource("/view/StartPage.fxml"));
        Scene scene = new Scene(parent);
        primarystage.setScene(scene);
        primarystage.setTitle("Life Fitness Gym");
        primarystage.show();
    }
}
