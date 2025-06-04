package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.lifefitnessgym.dto.CashierDto;
import lk.ijse.lifefitnessgym.dto.tm.CashierTm;
import lk.ijse.lifefitnessgym.dto.tm.MemberTm;
import lk.ijse.lifefitnessgym.model.CashierModel;
import lk.ijse.lifefitnessgym.model.MemberModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class CashierPageController implements Initializable {

    public TextField txtCashierName;
    public TextField txtCashierContact;
    public Button btnReset;
    public Label lblCashierId;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;

    public TableView<CashierTm> CashierTable;
    public TableColumn<CashierTm, String> colCashierId;
    public TableColumn<CashierTm, String> colCashierName;
    public TableColumn<CashierTm, String> colCashierContact;

    private CashierModel cashierModel = new CashierModel();

    private final String namePattern = "^[A-Za-z ]+$";
    private final String phonePattern = "^(?:0|\\+94)(7[0-9]|1[0-9]|2[0-9])\\d{7}$";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colCashierId.setCellValueFactory(new PropertyValueFactory<>("cashierId"));
        colCashierName.setCellValueFactory(new PropertyValueFactory<>("cashierName"));
        colCashierContact.setCellValueFactory(new PropertyValueFactory<>("contact"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }
    private void loadTableData() throws SQLException {
        CashierTable.setItems(FXCollections.observableArrayList(
                cashierModel.getAllCashier().stream()
                        .map(cashierDto -> new CashierTm(
                                cashierDto.getCashierId(),
                                cashierDto.getCashierName(),
                                cashierDto.getContact()
                        )).toList()
        ));
    }
    private void loadNextId() throws SQLException {
        String nextId =cashierModel.getNextCashierId();
        lblCashierId.setText(nextId);
    }

    private void resetPage(){
        try {
            loadTableData();
            loadNextId();
            btnSave.setDisable(false);

            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

            txtCashierName.setText("");
            txtCashierContact.setText("");
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }
    public void btnSaveCashierOnAction(ActionEvent actionEvent) {
        String cashierId = lblCashierId.getText();
        String cashierName = txtCashierName.getText();
        String cashierContact = txtCashierContact.getText();

        boolean isValidName = cashierName.matches(namePattern);
        boolean isValidContact = cashierContact.matches(phonePattern);

        txtCashierName.setStyle(txtCashierName.getStyle() + ";-fx-border-color: #7367F0;");
        txtCashierContact.setStyle(txtCashierContact.getStyle() + ";-fx-border-color: #7367F0;");

        if(!isValidName) txtCashierName.setStyle(txtCashierName.getStyle() + ";-fx-border-color: red;");
        if(!isValidContact) txtCashierContact.setStyle(txtCashierContact.getStyle() + ";-fx-border-color: red;");

        CashierDto cashierDto = new CashierDto(
                cashierId,
                cashierName,
                cashierContact
        );
        if(isValidName && isValidContact) {
            try {
                boolean isSaved = cashierModel.saveCashier(cashierDto);
                if (isSaved) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Cashier saved successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to save cashier.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to save cashier.").show();
            }
        }
    }

    public void btnUpdateCashierOnAction(ActionEvent actionEvent) {
        String cashierId = lblCashierId.getText();
        String cashierName = txtCashierName.getText();
        String cashierContact = txtCashierContact.getText();

        CashierDto cashierDto= new CashierDto(
                cashierId,
                cashierName,
                cashierContact
        );
        try {
            boolean isUpdated = cashierModel.updateCashier(cashierDto);
            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Cashier updated successfully.").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Fail to update cashier.").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update cashier.").show();
        }
    }

    public void btnDeleteCashierOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String cashierId = lblCashierId.getText();
            try {
                boolean isDeleted = cashierModel.deleteCashier(cashierId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Cashier deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete cashier.").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete cashier.").show();
            }
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        CashierTm selectedItem =CashierTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblCashierId.setText(selectedItem.getCashierId());
            txtCashierName.setText(selectedItem.getCashierName());
            txtCashierContact.setText(selectedItem.getContact());

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void btnResetPageOnAction(ActionEvent actionEvent) {
        resetPage();
    }


}
