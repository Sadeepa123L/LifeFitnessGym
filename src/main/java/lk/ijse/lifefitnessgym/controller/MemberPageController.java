package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.dto.tm.MemberTm;
import lk.ijse.lifefitnessgym.model.MemberModel;

import javax.swing.JOptionPane;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemberPageController implements Initializable {


    public Label lblId;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtContact;

    public TableView<MemberTm> memberTable;
    public TableColumn<MemberTm, String> colId;
    public TableColumn<MemberTm, String> colName;
    public TableColumn<MemberTm, String> colAddress;
    public TableColumn<MemberTm, String> colContact;


    private MemberModel memberModel = new MemberModel();

    public Button btnSave;
    public Button btnUpdate;
    public Button btnDelete;
    public Button btnReset;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("memberAddress"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("memberContact"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    private void loadTableData() throws SQLException {
        memberTable.setItems(FXCollections.observableArrayList(
                memberModel.getAllMembers().stream()
                        .map(memberDto -> new MemberTm(
                            memberDto.getMemberId(),
                            memberDto.getMemberName(),
                            memberDto.getMemberAddress(),
                            memberDto.getMemberContact()
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

            txtName.setText("");
            txtAddress.setText("");
            txtContact.setText("");
        }catch (SQLException e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }

    public void btnSaveMemberOnAction(ActionEvent actionEvent) {
        String memberId = lblId.getText();
        String memberName = txtName.getText();
        String memberAddress = txtAddress.getText();
        String memberContact = txtContact.getText();

        MemberDto memberDto = new MemberDto(
                memberId,
                memberName,
                memberAddress,
                memberContact
        );
        try {
            boolean isSaved = memberModel.saveMember(memberDto);

            if (isSaved) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Member saved successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to save member.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to save member.").show();
        }

    }

    public void btnUpdateMemberOnAction(ActionEvent actionEvent) {
        String memberId = lblId.getText();
        String memberName = txtName.getText();
        String memberAddress = txtAddress.getText();
        String memberContact = txtContact.getText();
        MemberDto memberDto = new MemberDto(
                memberId,
                memberName,
                memberAddress,
                memberContact
        );

        try {
            boolean isUpdated = memberModel.updateMember(memberDto);
            if (isUpdated) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Member updated successfully.").show();
            }else {
                new Alert(Alert.AlertType.ERROR, "Fail to update member.").show();
            }


        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to update member.").show();
        }
    }

    public void btnDeleteMemberOnAction(ActionEvent actionEvent) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Are you sure ?",
                ButtonType.YES,
                ButtonType.NO
        );

        Optional<ButtonType> response = alert.showAndWait();

        if (response.isPresent() && response.get() == ButtonType.YES) {
            String memberId = lblId.getText();
            try {
                boolean isDeleted = memberModel.deleteMember(memberId);
                if (isDeleted) {
                    resetPage();
                    new Alert(Alert.AlertType.INFORMATION, "Member deleted successfully.").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Fail to delete member.").show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, "Fail to delete member.").show();
            }
        }
    }

    private void loadNextId() throws SQLException {
        String nextId =memberModel.getNextMemberId();
        lblId.setText(nextId);
    }

    public void onClickTable(MouseEvent mouseEvent) {
        MemberTm selectedItem =memberTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            lblId.setText(selectedItem.getMemberId());
            txtName.setText(selectedItem.getMemberName());
            txtAddress.setText(selectedItem.getMemberAddress());
            txtContact.setText(selectedItem.getMemberContact());

            btnSave.setDisable(true);

            btnUpdate.setDisable(false);
            btnDelete.setDisable(false);
        }
    }

    public void btnResetPageOnAction(ActionEvent actionEvent) {
        resetPage();
    }
}

