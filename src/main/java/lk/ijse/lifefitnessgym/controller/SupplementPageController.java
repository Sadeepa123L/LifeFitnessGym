package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.dto.SupplementDto;
import lk.ijse.lifefitnessgym.dto.tm.MemberTm;
import lk.ijse.lifefitnessgym.dto.tm.SupplementTm;
import lk.ijse.lifefitnessgym.model.SupplementModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class SupplementPageController implements Initializable {
    public TextField txtSuppType;
    public TextField txtSuppName;
    public TextField txtSuppQty;
    public Label lblSuppId;

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;

    public TableView<SupplementTm> supplementTable;
    public TableColumn<SupplementTm, String > colSuppId;
    public TableColumn<SupplementTm, String> colSuppType;
    public TableColumn<SupplementTm, String > colSuppName;
    public TableColumn<SupplementTm, String> colSuppQty;
    public TableColumn<SupplementTm, Double> colSuppPrice;
    public TextField txtSuppPrice;

    private SupplementModel supplementModel = new SupplementModel();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSuppId.setCellValueFactory(new PropertyValueFactory<>("supplementId"));
        colSuppType.setCellValueFactory(new PropertyValueFactory<>("supplementType"));
        colSuppName.setCellValueFactory(new PropertyValueFactory<>("supplementName"));
        colSuppQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colSuppPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }
    private void loadTableData() throws SQLException {
        supplementTable.setItems(FXCollections.observableArrayList(
                supplementModel.getAllSupplement().stream()
                        .map(supplementDto -> new SupplementTm(
                                supplementDto.getSupplementId(),
                                supplementDto.getSupplementType(),
                                supplementDto.getSupplementName(),
                                supplementDto.getQty(),
                                supplementDto.getPrice()
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

            txtSuppType.setText("");
            txtSuppName.setText("");
            txtSuppQty.setText("");
            txtSuppPrice.setText("");
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    private void loadNextId() throws SQLException {
        String nextId =supplementModel.getNextSupplementId();
        lblSuppId.setText(nextId);
    }
    public void btnSaveSuppOnAction(ActionEvent actionEvent) {
        String suppId = lblSuppId.getText();
        String suppType = txtSuppType.getText();
        String suppName = txtSuppName.getText();
        String qty = txtSuppQty.getText();
        String p = txtSuppPrice.getText();
        Double price = Double.parseDouble(p);

        SupplementDto supplementDto = new SupplementDto(
                suppId,
                suppType,
                suppName,
                qty,
                price
        );
        try {
            boolean isSaved = supplementModel.saveSupplement(supplementDto);

            if (isSaved) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplement saved successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save supplement.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save supplement.").show();
        }
    }

    public void btnUpdateSuppOnAction(ActionEvent actionEvent) throws SQLException {
        String suppId = lblSuppId.getText();
        String suppType =txtSuppType.getText();
        String suppName = txtSuppName.getText();
        String qty = txtSuppQty.getText();
        String p = txtSuppPrice.getText();
        Double price = Double.parseDouble(p);
        SupplementDto supplementDto = new SupplementDto(
                suppId,
                suppType,
                suppName,
                qty,
                price
        );
        try {
            boolean isUpdate =supplementModel.updateSupplement(supplementDto);
            if (isUpdate) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Supplement updated successfully.").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Fail to update supplement.").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update supplement.").show();
        }

    }

    public void btnDeleteSuppOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String suppId = lblSuppId.getText();
            try {
                boolean isDeleted = supplementModel.deleteSupplement(suppId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Supplement deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete supplement.").show();
                }
            }catch (Exception e){
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete supplement.").show();
            }
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        SupplementTm selectedItem =supplementTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblSuppId.setText(selectedItem.getSupplementId ());
            txtSuppType.setText(selectedItem.getSupplementType());
            txtSuppName.setText(selectedItem.getSupplementName());
            txtSuppQty.setText(selectedItem.getQty());
            txtSuppPrice.setText(String.valueOf(selectedItem.getPrice()));

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void btnResetPageOnAction(ActionEvent actionEvent) {
        resetPage();
    }


}
