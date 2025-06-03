package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.lifefitnessgym.dto.MembershipDto;
import lk.ijse.lifefitnessgym.dto.tm.DietPlanTm;
import lk.ijse.lifefitnessgym.dto.tm.MemberTm;
import lk.ijse.lifefitnessgym.dto.tm.MembershipTm;
import lk.ijse.lifefitnessgym.model.MembershipModel;

import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class MembershipPageController implements Initializable {
    public TextField txtMembershipType;
    public TextField txtPeriod;
    public TextField txtPrice;
    public Label lblMembershipId;

    public Button btnReset;
    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;

    public TableView<MembershipTm> membershipTable;
    public TableColumn<MembershipTm, String> colMembershipId;
    public TableColumn<MembershipTm, String> colType;
    public TableColumn<MembershipTm, String> colPeriod;
    public TableColumn<MembershipTm, Double> colPrice;

    private MembershipModel membershipModel = new MembershipModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colMembershipId.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        colType.setCellValueFactory(new PropertyValueFactory<>("membershipType"));
        colPeriod.setCellValueFactory(new PropertyValueFactory<>("validPeriod"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }
    private void loadTableData() throws SQLException {
        membershipTable.setItems(FXCollections.observableArrayList(
                membershipModel.getAllMembership().stream()
                        .map(membershipDto -> new MembershipTm(
                                membershipDto.getMembershipId(),
                                membershipDto.getMembershipType(),
                                membershipDto.getValidPeriod(),
                                membershipDto.getPrice()
                        )).toList()
        ));
    }
    private void loadNextId() throws SQLException {
        String nextId=membershipModel.getNextMembershipId();
        lblMembershipId.setText(nextId);
    }
    public void resetPage(){
        try{
            loadTableData();
            loadNextId();

            txtMembershipType.setText("");
            txtPeriod.setText("");
            txtPrice.setText("");

            btnSave.setDisable(false);

            btnUpdate.setDisable(true);
            btnDelete.setDisable(true);

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    public void btnSaveMembershipOnAction(ActionEvent actionEvent) {
        String membershipId = lblMembershipId.getText();
        String membershipType = txtMembershipType.getText();
        String validPeriod = txtPeriod.getText();

        String p = txtPrice.getText();
        Double price = Double.parseDouble(p);

        MembershipDto membershipDto = new MembershipDto(
                membershipId,
                membershipType,
                validPeriod,
                price
        );
        try {
            boolean isSaved = membershipModel.saveMembership(membershipDto);
            if (isSaved) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Membership saved successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save membership.").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to save membership.").show();
        }
    }

    public void btnUpdateMembershipOnAction(ActionEvent actionEvent) {
        String membershipId = lblMembershipId.getText();
        String membershipType = txtMembershipType.getText();
        String validPeriod = txtPeriod.getText();

        String p = txtPrice.getText();
        Double price = Double.parseDouble(p);

        MembershipDto membershipDto = new MembershipDto(
                membershipId,
                membershipType,
                validPeriod,
                price
        );
        try {
            boolean isUpdate = membershipModel.updateMembership(membershipDto);
            if (isUpdate) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Membership updated successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to update membership.").show();
            }
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Fail to update membership.").show();
        }
    }

    public void btnDeleteMembershipOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );
        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String membershipId = lblMembershipId.getText();
            try {
                boolean isDeleted = membershipModel.deleteMembership(membershipId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Membership deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete membership.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete membership.").show();
            }
        }
    }

    public void onClickTable(MouseEvent mouseEvent) {
        MembershipTm selectedItem =membershipTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblMembershipId.setText(selectedItem.getMembershipId ());
            txtMembershipType.setText(selectedItem.getMembershipType ());
            txtPeriod.setText(selectedItem.getValidPeriod());
            txtPrice.setText(String.valueOf(selectedItem.getPrice()));

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void btnResetPageOnAction(ActionEvent actionEvent) {
        resetPage();
    }


}
