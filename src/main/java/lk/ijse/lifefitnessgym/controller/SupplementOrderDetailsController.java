package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.lifefitnessgym.db.DBConnection;
import lk.ijse.lifefitnessgym.dto.tm.DietPlanOrderTm;
import lk.ijse.lifefitnessgym.dto.tm.SupplementOrderTm;
import lk.ijse.lifefitnessgym.model.SupplementOrderDetailsModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SupplementOrderDetailsController implements Initializable {
    public TableView<SupplementOrderTm> orderDetailsTable;
    public TableColumn<SupplementOrderTm, String> colOrderId;
    public TableColumn<SupplementOrderTm, String> colMemberId;
    public TableColumn<SupplementOrderTm, String > colCashierId;
    public TableColumn<SupplementOrderTm, String> colSId;
    public TableColumn<SupplementOrderTm, Double> colOTotal;
    public TableColumn<SupplementOrderTm, String> colDate;
    public TableColumn<SupplementOrderTm, Double> colQty;

    SupplementOrderDetailsModel supplementOrderDetailsModel = new SupplementOrderDetailsModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colOrderId.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        colMemberId.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        colCashierId.setCellValueFactory(new PropertyValueFactory<>("cashierId"));
        colSId.setCellValueFactory(new PropertyValueFactory<>("supplementId"));
        colOTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        try {
            resetPage();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }
    private void resetPage(){
        try {
            loadTableData();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void loadTableData() throws SQLException {
        orderDetailsTable.setItems(FXCollections.observableArrayList(
                supplementOrderDetailsModel.getAllSupplementOrders().stream()
                        .map(cartDto -> new SupplementOrderTm(
                                cartDto.getOrderId(),
                                cartDto.getMemberId(),
                                cartDto.getCashierId(),
                                cartDto.getSupplementId(),
                                cartDto.getTotal(),
                                cartDto.getQuantity(),
                                cartDto.getDate()
                        )).toList()
        ));
    }

    public void btnGenerateReportOnAction(ActionEvent actionEvent) {
        try {
            JasperReport report = JasperCompileManager.compileReport(
                    getClass().getResourceAsStream("/Reports/SupplementOrder.jrxml")
            );
            Connection connection = DBConnection.getInstance().getConnection();
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    report,
                    parameters,
                    connection
            );
            JasperViewer.viewReport(jasperPrint, false);

            // List
            // Array list
            // index value
            // 0 - ""
            // 1 - ""

            // Map
            // HashMap
            // key value
            // "hello" - "hi"

//            JasperFillManager.fillReport(
//                    report,
//                    null,
//                    connection
//            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
