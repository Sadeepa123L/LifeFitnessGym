package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.lifefitnessgym.dto.DietPlanDto;
import lk.ijse.lifefitnessgym.dto.tm.DietPlanTm;
import lk.ijse.lifefitnessgym.dto.tm.MemberTm;
import lk.ijse.lifefitnessgym.model.DietPlanModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class DietPlanPageController implements Initializable {


    public Label lblDietId;
    public TextField txtDietName;
    public TextField txtPurpose;
    public TextField txtPrice;

    public TableView<DietPlanTm> dietTable;
    public TableColumn<DietPlanTm, String> colDietId;
    public TableColumn<DietPlanTm, String> colDietName;
    public TableColumn<DietPlanTm, String> colPurpose;
    public TableColumn<DietPlanTm, Double> colPrice;

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    private DietPlanModel dietPlanModel = new DietPlanModel();

    private final String namePattern = "^[A-Za-z ]+$";
    private final String purposePattern = "^[A-Za-z ]+$";
    //private final String pricePattern = "^([1-9]\\d*(\\.\\d+)?|0\\.\\d*[1-9])$";
            ;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colDietId.setCellValueFactory(new PropertyValueFactory<>("dietId"));
        colDietName.setCellValueFactory(new PropertyValueFactory<>("dietName"));
        colPurpose.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    public void btnSaveDietOnAction(ActionEvent actionEvent) {
        String payment = txtPrice.getText();

        String dietId = lblDietId.getText();
        String dietName = txtDietName.getText();
        String purpose = txtPurpose.getText();
        Double price = Double.parseDouble(payment);

        boolean isValidName = namePattern.matches(namePattern);
        boolean isValidPurpose = purposePattern.matches(purposePattern);
        //boolean isValidPrice = pricePattern.matches(pricePattern);

        txtDietName.setStyle(txtDietName.getStyle() + ";-fx-border-color: #7367F0;");
        txtPurpose.setStyle(txtPurpose.getStyle() + ";-fx-border-color: #7367F0;");
        //txtPrice.setStyle(txtPrice.getStyle() + ";-fx-border-color: #7367F0;");

        if(!isValidName) txtDietName.setStyle(txtDietName.getStyle() + ";-fx-border-color: red;");
        if(!isValidPurpose) txtPurpose.setStyle(txtPurpose.getStyle() + ";-fx-border-color: red;");
        //if(!isValidPrice) txtPrice.setStyle(txtPrice.getStyle() + ";-fx-border-color: red;");*/

        DietPlanDto dietPlanDto = new DietPlanDto(
                dietId,
                dietName,
                purpose,
                price
        );
        if(isValidName && isValidPurpose) {
            try {
                boolean isSaved = dietPlanModel.saveDietPlan(dietPlanDto);

                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Diet plan saved successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to save diet plan.").show();
                }

            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to save diet plan.").show();
            }
       }
    }

    public void btnUpdateDietOnAction(ActionEvent actionEvent) {
        String payment = txtPrice.getText();

        String dietId = lblDietId.getText();
        String dietName = txtDietName.getText();
        String purpose = txtPurpose.getText();
        Double price = Double.parseDouble(payment);

        DietPlanDto dietPlanDto = new DietPlanDto(
                dietId,
                dietName,
                purpose,
                price
        );

        try {
            boolean isUpdate = dietPlanModel.updateDietPlan(dietPlanDto);

            if(isUpdate){
                resetPage();
                new Alert(Alert.AlertType.INFORMATION,"Diet plan updated successfully.").show();
            }else{
                new Alert(Alert.AlertType.ERROR,"Fail to update diet plan.").show();
            }

        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to update diet plan.").show();
        }
    }

    public void btnDeleteDietOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String dietId = lblDietId.getText();
            try {
                boolean isDeleted = dietPlanModel.deleteDietPlan(dietId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Diet Plan deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete diet plan.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete diet plan.").show();
            }
        }
    }

    public void btnResetPageOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void onClickTable(MouseEvent mouseEvent) {
        DietPlanTm selectedItem =dietTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblDietId.setText(selectedItem.getDietId());
            txtDietName.setText(selectedItem.getDietName());
            txtPurpose.setText(selectedItem.getPurpose());
            txtPrice.setText(String.valueOf(selectedItem.getPrice()));

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    private void loadTableData() throws SQLException {
        dietTable.setItems(FXCollections.observableArrayList(
                dietPlanModel.getAllDietPlan().stream()
                        .map(dietPlanDto -> new DietPlanTm(
                            dietPlanDto.getDietId(),
                            dietPlanDto.getDietName(),
                            dietPlanDto.getPurpose(),
                            dietPlanDto.getPrice()
                )).toList()
        ));
    }

    private void resetPage(){
        try {
            loadTableData();
            loadNextId();

            txtDietName.setText("");
            txtPurpose.setText("");
            txtPrice.setText("");

            btnSave.setDisable(false);

            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void loadNextId() throws SQLException {
        String nextId =dietPlanModel.getNextDietId();
        lblDietId.setText(nextId);
    }
}
