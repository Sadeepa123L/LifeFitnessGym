package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.lifefitnessgym.dto.DietPlanDto;
import lk.ijse.lifefitnessgym.dto.DietPlanOrderDto;
import lk.ijse.lifefitnessgym.dto.WorkoutSessionDto;
import lk.ijse.lifefitnessgym.dto.tm.DietPlanOrderTm;
import lk.ijse.lifefitnessgym.dto.tm.DietPlanTm;
import lk.ijse.lifefitnessgym.model.DietPlanModel;
import lk.ijse.lifefitnessgym.model.DietPlanOrderModel;
import lk.ijse.lifefitnessgym.model.MemberModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DietPlanOrderController implements Initializable {
    public Label lblOrderId;
    public Label orderDate;
    public Label lblMemberName;
    public Label lblPlanName;
    public Label lblItemPrice;
    
    public ComboBox cmbMemberId;
    public ComboBox cmbPlanId;
   
    public TableView<DietPlanOrderTm> DietOrderTable;
    public TableColumn<DietPlanOrderTm, String> colOrderId;
    public TableColumn<DietPlanOrderTm, String> colMemberId;
    public TableColumn<DietPlanOrderTm, String> colPlanId;
    public TableColumn<DietPlanOrderTm, Double> colPrice;
    public TableColumn<DietPlanOrderTm, String> colDate;

    private DietPlanOrderModel dietPlanOrderModel = new DietPlanOrderModel();
    private final MemberModel memberModel = new MemberModel();
    private final DietPlanModel dietPlanModel = new DietPlanModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("paymentIdD"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colPlanId.setCellValueFactory(new PropertyValueFactory<>("planId"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        try {
            resetPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }
    private void loadNextId() throws SQLException {
        String nextId =dietPlanOrderModel.getNextOrderId();
        lblOrderId.setText(nextId);
    }
    private void resetPage(){
        try {
            loadMemberId();
            loadPlanId();
            loadTableData();
            loadNextId();
            orderDate.setText(LocalDate.now().toString());

            cmbPlanId.getSelectionModel().clearSelection();
            cmbMemberId.getSelectionModel().clearSelection();
            lblMemberName.setText("");
            lblPlanName.setText("");
            lblItemPrice.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void loadTableData() throws SQLException {
        DietOrderTable.setItems(FXCollections.observableArrayList(
                dietPlanOrderModel.getAllDietPlanOrder().stream()
                        .map(dietPlanOrderDto -> new DietPlanOrderTm(
                                dietPlanOrderDto.getPaymentIdD(),
                                dietPlanOrderDto.getMemberId(),
                                dietPlanOrderDto.getPlanId(),
                                dietPlanOrderDto.getDate(),
                                dietPlanOrderDto.getPrice()
                        )).toList()
        ));
    }

    private void loadMemberId() throws SQLException {
        ArrayList<String> memberIdList = memberModel.getAllMemberIds();
        ObservableList<String> memberIds = FXCollections.observableArrayList();
        memberIds.addAll(memberIdList);
        cmbMemberId.setItems(memberIds);
    }
    private void loadPlanId() throws SQLException {
        ArrayList<String> planIdList = dietPlanModel.getAllPlanIds();
        ObservableList<String> planIds = FXCollections.observableArrayList();
        planIds.addAll(planIdList);
        cmbPlanId.setItems(planIds);
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


        DietPlanOrderDto dietPlanOrderDto = new DietPlanOrderDto(
                orderId,
                memberId,
                planId,
                date,
                price
        );
        try {
            boolean isSaved = dietPlanOrderModel.placeOrder(dietPlanOrderDto);

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

    public void cmbMemberOnAction(ActionEvent actionEvent) throws SQLException {
        String selectedMemberId = (String) cmbMemberId.getSelectionModel().getSelectedItem();
        String selectedMemberName = memberModel.findNameById(selectedMemberId);

        if(selectedMemberName != null){
            lblMemberName.setText(selectedMemberName);
        }else{
            lblMemberName.setText("");
        }
    }

    public void cmbPlanOnAction(ActionEvent actionEvent) throws SQLException {
        String selectedDietId = (String) cmbPlanId.getSelectionModel().getSelectedItem();
        DietPlanDto dietPlanDto = dietPlanModel.findById(selectedDietId);

        if(dietPlanDto!= null){
            lblPlanName.setText(selectedDietId);
            lblItemPrice.setText(String.valueOf(dietPlanDto.getPrice()));
        }else{
            lblPlanName.setText("");
            lblItemPrice.setText("");
        }
    }


}
