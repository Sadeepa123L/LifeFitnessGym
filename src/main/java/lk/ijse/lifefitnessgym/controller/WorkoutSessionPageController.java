package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.dto.WorkoutSessionDto;
import lk.ijse.lifefitnessgym.dto.tm.MemberTm;
import lk.ijse.lifefitnessgym.dto.tm.WorkoutSessionTm;
import lk.ijse.lifefitnessgym.model.TrainerModel;
import lk.ijse.lifefitnessgym.model.WorkoutSessionModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class WorkoutSessionPageController implements Initializable {
    public TableView<WorkoutSessionTm> SessionTable;
    public TableColumn<WorkoutSessionTm, String> colSessionId;
    public TableColumn<WorkoutSessionTm, String> colTrainerId;
    public TableColumn<WorkoutSessionTm, String> colSessionName;
    public TableColumn<WorkoutSessionTm, Double> colPayment;
    public TableColumn<WorkoutSessionTm, String> colTime;


    WorkoutSessionModel workoutSessionModel = new WorkoutSessionModel();

    public Label lblSessionId;
    public Label lblTrainerId;
    public TextField txtSessionName;
    public TextField txtPayment;
    public TextField txtTimeTable;


    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    public ComboBox<String> cmbTrainerId;

    private final TrainerModel trainerModel = new TrainerModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSessionId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colTrainerId.setCellValueFactory(new PropertyValueFactory<>("trainerId"));
        colSessionName.setCellValueFactory(new PropertyValueFactory<>("sessionName"));
        colPayment.setCellValueFactory(new PropertyValueFactory<>("paymentPerHour"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("timeTable"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }

    }

    private void loadTableData() throws SQLException {
        SessionTable.setItems(FXCollections.observableArrayList(
                workoutSessionModel.getAllSession().stream()
                        .map(workoutSessionDto -> new WorkoutSessionTm(
                                workoutSessionDto.getSessionId(),
                                workoutSessionDto.getTrainerId(),
                                workoutSessionDto.getSessionName(),
                                workoutSessionDto.getPaymentPerHour(),
                                workoutSessionDto.getTimeTable()
                        )).toList()
        ));
    }

    private void loadTrainerId() throws SQLException {
        ArrayList<String> trainerIdsList = trainerModel.getAllTrainerIds();
        ObservableList<String> trainerIds = FXCollections.observableArrayList();
        trainerIds.addAll(trainerIdsList);
        cmbTrainerId.setItems(trainerIds);
    }

    private void resetPage(){
        try {
            loadTrainerId();
            loadTableData();
            loadNextId();
            btnSave.setDisable(false);

            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            txtSessionName.setText("");
            txtPayment.setText("");
            txtTimeTable.setText("");
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }
    private void loadNextId() throws SQLException {
        String nextId =workoutSessionModel.getNextSessionId();
        lblSessionId.setText(nextId);
    }

    public void btnSaveSessionOnAction(ActionEvent actionEvent) {
        String payment = txtPayment.getText();

        String sessionId = lblSessionId.getText();
        String trainerId = cmbTrainerId.getSelectionModel().getSelectedItem();
        String sessionName = txtSessionName.getText();
        Double paymentPerHour = Double.parseDouble(payment);
        String timeTable = txtTimeTable.getText();

        WorkoutSessionDto workoutSessionDto= new WorkoutSessionDto(
                sessionId,
                trainerId,
                sessionName,
                paymentPerHour,
                timeTable
        );
        try {
            boolean isSaved = workoutSessionModel.saveSession(workoutSessionDto);

            if (isSaved) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Workout Session saved successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save workout session.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save workout session.").show();
        }
    }

    public void btnUpdateSessionOnAction(ActionEvent actionEvent) {
        String payment = txtPayment.getText();

        String sessionId = lblSessionId.getText();
        String trainerId = cmbTrainerId.getSelectionModel().getSelectedItem();
        String sessionName = txtSessionName.getText();
        Double paymentPerHour = Double.parseDouble(payment);
        String timeTable = txtTimeTable.getText();

        WorkoutSessionDto workoutSessionDto= new WorkoutSessionDto(
                sessionId,
                trainerId,
                sessionName,
                paymentPerHour,
                timeTable
        );
        try {
            boolean isUpdate = workoutSessionModel.updateSession(workoutSessionDto);

            if (isUpdate) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Workout Session updated successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update workout session.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update workout session.").show();
        }
    }

    public void btnDeleteDeleteOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String sessionId = lblSessionId.getText();
            try {
                boolean isDeleted = workoutSessionModel.deleteSession(sessionId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Workout session deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete workout session.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete workout session.").show();
            }
        }
    }

    public void btnResetPageOnAction(ActionEvent actionEvent) {
        try {
            loadTableData();
            loadNextId();
            btnSave.setDisable(false);

            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            lblTrainerId.setText("");
            txtSessionName.setText("");
            txtPayment.setText("");
            txtTimeTable.setText("");
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }


    public void onClickTable(MouseEvent mouseEvent) {
        WorkoutSessionTm selectedItem = (WorkoutSessionTm) SessionTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblSessionId.setText(selectedItem.getSessionId());
            lblTrainerId.setText(selectedItem.getTrainerId());
            txtSessionName.setText(selectedItem.getSessionName());
            txtPayment.setText(String.valueOf(selectedItem.getPaymentPerHour()));
            txtTimeTable.setText(selectedItem.getTimeTable());

            btnSave.setDisable(true);
            //cmbTrainerId.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void btnTrainerOnAction(ActionEvent actionEvent) {
        String selectedTrainerId = cmbTrainerId.getSelectionModel().getSelectedItem();
    }
}
