package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.lifefitnessgym.dto.DietPlanDto;
import lk.ijse.lifefitnessgym.dto.ReservationDto;
import lk.ijse.lifefitnessgym.dto.WorkoutSessionDto;
import lk.ijse.lifefitnessgym.dto.tm.DietPlanOrderTm;
import lk.ijse.lifefitnessgym.dto.tm.ReservationTm;
import lk.ijse.lifefitnessgym.model.MemberModel;
import lk.ijse.lifefitnessgym.model.ReservationModel;
import lk.ijse.lifefitnessgym.model.WorkoutSessionModel;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ReservationPageController implements Initializable {
    public Label lblOrderId;
    public Label orderDate;
    public Label lblMemberName;
    public Label lblSessionName;
    public Label lblSessionPrice;

    public ComboBox cmbMemberId;
    public ComboBox cmbSessionId;

    public TableView<ReservationTm> SessionTable;
    public TableColumn<ReservationTm, String> colOrderId;
    public TableColumn<ReservationTm, String> colMemberId;
    public TableColumn<ReservationTm, String> colSessionId;
    public TableColumn<ReservationTm, Double> colSessionPrice;
    public TableColumn<ReservationTm, Date> colDate;
    public TableColumn<ReservationTm, String> colTime;
    public TextField txtSessionTime;

    ReservationModel reservationModel = new ReservationModel();
    MemberModel memberModel = new MemberModel();
    WorkoutSessionModel workoutSessionModel = new WorkoutSessionModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colSessionId.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colSessionPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Something went wrong").show();
        }
    }
    private void loadNextId() throws SQLException {
        String nextId =reservationModel.getNextOrderId();
        lblOrderId.setText(nextId);
    }
    private void loadTableData() throws SQLException {
        SessionTable.setItems(FXCollections.observableArrayList(
                reservationModel.getAllReservations().stream()
                        .map(reservationDto -> new ReservationTm(
                                reservationDto.getOrderId(),
                                reservationDto.getMemberId(),
                                reservationDto.getSessionId(),
                                reservationDto.getTime(),
                                reservationDto.getPrice(),
                                reservationDto.getDate()
                        )).toList()
        ));
    }
    private void loadMemberId() throws SQLException {
        ArrayList<String> memberIdList = memberModel.getAllMemberIds();
        ObservableList<String> memberIds = FXCollections.observableArrayList();
        memberIds.addAll(memberIdList);
        cmbMemberId.setItems(memberIds);
    }
    private void loadSessionId() throws SQLException {
        ArrayList<String> sessionIdList = workoutSessionModel.getAllSessionIds();
        ObservableList<String> sessionIds = FXCollections.observableArrayList();
        sessionIds.addAll(sessionIdList);
        cmbSessionId.setItems(sessionIds);
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
    private void resetPage(){
        try {
            loadMemberId();
            loadSessionId();
            loadTableData();
            loadNextId();
            orderDate.setText(LocalDate.now().toString());

            txtSessionTime.setText("");
            cmbMemberId.getSelectionModel().clearSelection();
            lblMemberName.setText("");
            cmbSessionId.getSelectionModel().clearSelection();
            lblSessionName.setText("");
            lblSessionPrice.setText("");

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
        resetPage();
    }

    public void btnReserveOnAction(ActionEvent actionEvent) {
        String orderId = lblOrderId.getText();
        String memberId = (String) cmbMemberId.getSelectionModel().getSelectedItem();
        String sessionId = (String) cmbSessionId.getSelectionModel().getSelectedItem();
        String date = orderDate.getText();

        String t = txtSessionTime.getText();
        int time = Integer.parseInt(t);

        String payment = lblSessionPrice.getText();

        Double price = Double.parseDouble(payment);

        Double total = price * time;
        txtSessionTime.setStyle("");


        ReservationDto reservationDto = new ReservationDto(
                orderId,
                memberId,
                sessionId,
                time,
                total,
                date
        );
        try {
            boolean isSaved = reservationModel.placeOrder(reservationDto);

            if (isSaved) {
                resetPage();
                new Alert(Alert.AlertType.INFORMATION, "Reserve successfully.").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Fail to reserve session.").show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Fail to reserve session.").show();
        }
    }

    public void cmbSessionOnAction(ActionEvent actionEvent) throws SQLException {
        String selectedSessionId = (String) cmbSessionId.getSelectionModel().getSelectedItem();
        WorkoutSessionDto workoutSessionDto = workoutSessionModel.findById(selectedSessionId);

        if(workoutSessionDto!= null){
            lblSessionName.setText(workoutSessionDto.getSessionName());
            lblSessionPrice.setText(String.valueOf(workoutSessionDto.getPaymentPerHour()));
        }else{
            lblSessionName.setText("");
            lblSessionPrice.setText("");
        }
    }


}
