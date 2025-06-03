package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.dto.TrainerDto;
import lk.ijse.lifefitnessgym.dto.tm.MemberTm;
import lk.ijse.lifefitnessgym.dto.tm.TrainerTm;
import lk.ijse.lifefitnessgym.model.MemberModel;
import lk.ijse.lifefitnessgym.model.TrainerModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class TrainerPageController implements Initializable {
    public TextField txtTrainerName;
    public TextField txtTrainerContact;
    public Label lblTrainerId;


    public TableView<TrainerTm> TrainerTable;
    public TableColumn<TrainerTm, String> colTrainerId;
    public TableColumn<TrainerTm, String> colTrainerName;
    public TableColumn<TrainerTm, String> colTrainerContact;

    private TrainerModel trainerModel = new TrainerModel();


    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colTrainerId.setCellValueFactory(new PropertyValueFactory<>("trainerId"));
        colTrainerName.setCellValueFactory(new PropertyValueFactory<>("trainerName"));
        colTrainerContact.setCellValueFactory(new PropertyValueFactory<>("trainerContact"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong", ButtonType.OK).show();
        }
    }
    private void loadTableData() throws SQLException {
        TrainerTable.setItems(FXCollections.observableArrayList(
                trainerModel.getAllTrainers().stream()
                        .map(trainerDto -> new TrainerTm(
                                trainerDto.getTrainerId(),
                                trainerDto.getTrainerName(),
                                trainerDto.getTrainerContact()
                        )).toList()
        ));
    }

    private void resetPage(){
        try {
            loadTableData();
            loadNextId();
            btnSave.setDisable(false);

            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            txtTrainerName.setText("");
            txtTrainerContact.setText("");
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    private void loadNextId() throws SQLException {
        String nextId =trainerModel.getNextTrainerId();
        lblTrainerId.setText(nextId);
    }

    public void btnSaveTrainerOnAction(ActionEvent actionEvent) {
        String trainerId = lblTrainerId.getText();
        String trainerName = txtTrainerName.getText();
        String trainerContact = txtTrainerContact.getText();

        TrainerDto trainerDto = new TrainerDto(
                trainerId,
                trainerName,
                trainerContact
        );

        try {
            boolean isSaved = trainerModel.saveTrainer(trainerDto);

            if (isSaved) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Trainer saved successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save trainer.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save trainer.").show();
        }
    }

    public void btnUpdateTrainerOnAction(ActionEvent actionEvent) {
        String trainerId = lblTrainerId.getText();
        String trainerName = txtTrainerName.getText();
        String trainerContact = txtTrainerContact.getText();
        TrainerDto trainerDto= new TrainerDto(
                trainerId,
                trainerName,
                trainerContact
        );

        try {
            boolean isUpdated = trainerModel.updateTrainer(trainerDto);
            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Trainer updated successfully.").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Fail to update trainer.").show();
            }


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update trainer.").show();
        }
    }

    public void btnDeleteTrainerOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String trainerId = lblTrainerId.getText();
            try {
                boolean isDeleted = trainerModel.deleteTrainer(trainerId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Trainer deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete trainer.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete trainer.").show();
            }
        }
    }

    public void btnResetPageOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        TrainerTm selectedItem = (TrainerTm) TrainerTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblTrainerId.setText(selectedItem.getTrainerId());
            txtTrainerName.setText(selectedItem.getTrainerName());
            txtTrainerContact.setText(selectedItem.getTrainerContact());

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }
    }

