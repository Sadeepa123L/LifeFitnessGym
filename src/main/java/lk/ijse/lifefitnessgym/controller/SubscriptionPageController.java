package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.lifefitnessgym.dto.DietPlanDto;
import lk.ijse.lifefitnessgym.dto.DietPlanOrderDto;
import lk.ijse.lifefitnessgym.dto.MembershipDto;
import lk.ijse.lifefitnessgym.dto.SubscriptionDto;
import lk.ijse.lifefitnessgym.dto.tm.DietPlanOrderTm;
import lk.ijse.lifefitnessgym.dto.tm.SubscriptionTm;
import lk.ijse.lifefitnessgym.model.MemberModel;
import lk.ijse.lifefitnessgym.model.MembershipModel;
import lk.ijse.lifefitnessgym.model.SubscriptionModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SubscriptionPageController implements Initializable {
    public Label lblOrderId;
    public Label orderDate;
    public Label lblMemberName;
    public Label lblPlanName;
    public Label lblItemPrice;

    public ComboBox cmbPlanId;
    public ComboBox cmbMemberId;

    public TableView<SubscriptionTm> SubscriptionTable;
    public TableColumn<SubscriptionTm, String> colOrderId;
    public TableColumn<SubscriptionTm, String> colMemberId;
    public TableColumn<SubscriptionTm, String> colMembershipId;
    public TableColumn<SubscriptionTm, Double> colPrice;
    public TableColumn<SubscriptionTm, String > colDate;
    public TableColumn<SubscriptionTm, String> endDate;
    public TextField txtEndDate;

    SubscriptionModel subscriptionModel = new SubscriptionModel();
    MemberModel memberModel = new MemberModel();
    MembershipModel membershipModel = new MembershipModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colMembershipId.setCellValueFactory(new PropertyValueFactory<>("membershipId"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        endDate.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }

    }
    public void cmbMemberOnAction(ActionEvent actionEvent) throws SQLException {
        String selectedMemberId = (String) cmbMemberId.getSelectionModel().getSelectedItem();
        String selectedMemberName = memberModel.findNameById(selectedMemberId);

        if(selectedMemberName != null){
            lblMemberName.setText(selectedMemberName);
        }else{
            lblMemberName.setText("");
        }
    }

    private void loadMemberId() throws SQLException {
        ArrayList<String> memberIdList = memberModel.getAllMemberIds();
        ObservableList<String> memberIds = FXCollections.observableArrayList();
        memberIds.addAll(memberIdList);
        cmbMemberId.setItems(memberIds);
    }
    private void loadMembershipId() throws SQLException {
        ArrayList<String> membershipIdList = membershipModel.getAllMembershipIds();
        ObservableList<String> membershipIds = FXCollections.observableArrayList();
        membershipIds.addAll(membershipIdList);
        cmbPlanId.setItems(membershipIds);
    }

    public void cmbPlanOnAction(ActionEvent actionEvent) throws SQLException {
        String selectedMembershipId = (String) cmbPlanId.getSelectionModel().getSelectedItem();
        MembershipDto membershipDto = membershipModel.findById(selectedMembershipId);

        if(membershipDto!= null){
            lblPlanName.setText(membershipDto.getMembershipType());
            lblItemPrice.setText(String.valueOf(membershipDto.getPrice()));
        }else{
            lblPlanName.setText("");
            lblItemPrice.setText("");
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        String payment = lblItemPrice.getText();

        String orderId = lblOrderId.getText();
        String memberId = (String) cmbMemberId.getSelectionModel().getSelectedItem();
        String planId = (String) cmbPlanId.getSelectionModel().getSelectedItem();
        String date = orderDate.getText();
        Double price = Double.parseDouble(payment);
        String endDate = txtEndDate.getText();


        SubscriptionDto subscriptionDto = new SubscriptionDto(
                orderId,
                memberId,
                planId,
                price,
                date,
                endDate
        );

        try {
            boolean isSaved = subscriptionModel.placeOrder(subscriptionDto);

            if (isSaved) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Place order successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to place order.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to place order.").show();
        }
    }
    private void loadNextId() throws SQLException {
        String nextId =subscriptionModel.getNextOrderId();
        lblOrderId.setText(nextId);
    }
    private void loadTableData() throws SQLException {
        SubscriptionTable.setItems(FXCollections.observableArrayList(
                subscriptionModel.getAllSubscriptionOrder().stream()
                        .map(subscriptionDto -> new SubscriptionTm(
                                subscriptionDto.getOrderId(),
                                subscriptionDto.getMemberId(),
                                subscriptionDto.getMembershipId(),
                                subscriptionDto.getPrice(),
                                subscriptionDto.getDate(),
                                subscriptionDto.getEndDate()
                        )).toList()
        ));
    }
    private void resetPage(){
        try {
            loadMemberId();
            loadMembershipId();
            loadTableData();
            loadNextId();
            orderDate.setText(LocalDate.now().toString());

            cmbPlanId.getSelectionModel().clearSelection();
            lblMemberName.setText("");
            lblPlanName.setText("");
            lblItemPrice.setText("");
            txtEndDate.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

}
