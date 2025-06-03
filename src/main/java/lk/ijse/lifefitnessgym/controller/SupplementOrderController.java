package lk.ijse.lifefitnessgym.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.lifefitnessgym.dto.CartDto;
import lk.ijse.lifefitnessgym.dto.DietPlanOrderDto;
import lk.ijse.lifefitnessgym.dto.SupplementDto;
import lk.ijse.lifefitnessgym.dto.SupplementOrderDto;
import lk.ijse.lifefitnessgym.dto.tm.CartTm;
import lk.ijse.lifefitnessgym.model.CashierModel;
import lk.ijse.lifefitnessgym.model.MemberModel;
import lk.ijse.lifefitnessgym.model.SupplementModel;
import lk.ijse.lifefitnessgym.model.SupplementOrderModel;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SupplementOrderController implements Initializable {
    public Label lblSupOrderId;
    public Label orderDate;
    public Label lblMemberName;
    public Label lblCashierName;
    public Label lblSuppPrice;
    public Label lblSuppName;
    public Label lblSuppQty;
    public TextField txtAddToCartQty;

    public ComboBox cmbMemberId;
    public ComboBox cmbCashierId;
    public ComboBox cmbSupId;

    public TableView<CartTm> tblCart;
    public TableColumn<CartTm, String> colSuppId;
    public TableColumn<CartTm, String> colName;
    public TableColumn<CartTm, String> colQuantity;
    public TableColumn<CartTm, Double> colPrice;
    public TableColumn<CartTm, Double> colTotal;
    public TableColumn<?, ?> colAction;

    private final SupplementOrderModel supplementOrderModel = new SupplementOrderModel();
    private final MemberModel memberModel = new MemberModel();
    private final CashierModel cashierModel = new CashierModel();
    private final SupplementModel supplementModel = new SupplementModel();

    private final ObservableList<CartTm> cartData = FXCollections.observableArrayList();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colSuppId.setCellValueFactory(new PropertyValueFactory<>("supplementId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("supplementName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("cartQty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btnRemove"));

        tblCart.setItems(cartData);

        try {
            refreshPage();
        }catch (Exception e){
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
        }
    }

    private void loadNextId() throws SQLException {
        String nextId =supplementOrderModel.getNextSupplementOrderId();
        lblSupOrderId.setText(nextId);
    }
    private void loadMemberIds() throws SQLException {
        ArrayList<String> memberIdsList = memberModel.getAllMemberIds();
        ObservableList<String> memberIds = FXCollections.observableArrayList();
        memberIds.addAll(memberIdsList);
        cmbMemberId.setItems(memberIds);
    }

    private void loadCashierIds() throws SQLException {
        ArrayList<String> cashierIdsList = cashierModel.getAllCashierIds();
        ObservableList<String> cashierIds = FXCollections.observableArrayList();
        cashierIds.addAll(cashierIdsList);
        cmbCashierId.setItems(cashierIds);
    }

    private void loadSupplementIds() throws SQLException {
        ArrayList<String> supplementIdsList = supplementModel.getAllSupplementIds();
        ObservableList<String> supplementIds = FXCollections.observableArrayList();
        supplementIds.addAll(supplementIdsList);
        cmbSupId.setItems(supplementIds);
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

    public void cmbCashierOnAction(ActionEvent actionEvent) throws SQLException {
        String selectedCashierId = (String) cmbCashierId.getSelectionModel().getSelectedItem();
        String selectedCashierName = cashierModel.findNameById(selectedCashierId);

        if (selectedCashierName != null) {
            lblCashierName.setText(selectedCashierName);
        } else {
            lblCashierName.setText("");
        }
    }

    public void cmbSupplementOnAction(ActionEvent actionEvent) throws SQLException {
        String selectedSupplementId = (String) cmbSupId.getSelectionModel().getSelectedItem();
        SupplementDto supplementDto = supplementModel.findById(selectedSupplementId);

        if(supplementDto != null){
            lblSuppName.setText(supplementDto.getSupplementName());
            lblSuppPrice.setText(String.valueOf(supplementDto.getPrice()));
            lblSuppQty.setText(String.valueOf(supplementDto.getQty()));
        }else {
            lblSuppName.setText("");
            lblSuppPrice.setText("");
            lblSuppQty.setText("");
        }
    }

    private void refreshPage() throws SQLException {
        loadNextId();
        loadMemberIds();
        loadCashierIds();
        loadSupplementIds();
        orderDate.setText(LocalDate.now().toString());
    }

    public void btnAddToCartOnAction(ActionEvent actionEvent) {
        String selectedSupplementId = (String) cmbSupId.getSelectionModel().getSelectedItem();
        String cartQtyString = txtAddToCartQty.getText();
        orderDate.setText(LocalDate.now().toString());

        if (selectedSupplementId == null) {
            new Alert(Alert.AlertType.WARNING, "Please select supplement..!").show();
            return;
        }

        if (!cartQtyString.matches("^[0-9]+$")) {
            new Alert(Alert.AlertType.WARNING, "Please enter valid quantity..!").show();
            return;
        }

        String itemQtyOnStockString = lblSuppQty.getText();

        int cartQty = Integer.parseInt(cartQtyString);
        int QtyOnStock = Integer.parseInt(itemQtyOnStockString);

        if (QtyOnStock < cartQty) {
            new Alert(Alert.AlertType.WARNING, "Not enough item quantity..!").show();
            return;
        }

        String supplementName = lblSuppName.getText();
        String PriceString = lblSuppPrice.getText();

        double Price = Double.parseDouble(PriceString);
        double total = Price * cartQty;


        for (CartTm cartTM : cartData) {
            if (cartTM.getSupplementId().equals(selectedSupplementId)) {
                int newQty = cartTM.getCartQty() + cartQty;

                if (QtyOnStock < newQty) {
                    new Alert(Alert.AlertType.WARNING, "Not enough item quantity..!").show();
                    return;
                }
                txtAddToCartQty.setText("");
                cartTM.setCartQty(newQty);
                cartTM.setTotal(newQty * Price);
               // cartTM.setOrderDate(LocalDate.now().toString());

                tblCart.refresh();
                return;
            }
        }

        Button removeBtn = new Button("Remove");
        CartTm cartTm =  new CartTm(
                selectedSupplementId,
                supplementName,
                cartQty,
                Price,
                total,
                removeBtn
        );

        removeBtn.setOnAction(action -> {
            cartData.remove(cartTm);

            tblCart.refresh();
        });

        txtAddToCartQty.setText("");
        cartData.add(cartTm);
    }

    public void btnResetOnAction(ActionEvent actionEvent) {
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
        if (tblCart.getItems().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please add supplement to cart..!").show();
            return;
        }

//        String selectedCustomerId = cmbCustomerId.getSelectionModel().getSelectedItem();
        String selectedMemberId = (String) cmbMemberId.getValue();

//        if (selectedCustomerId == null){
        if (cmbMemberId.getSelectionModel().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please select member for place order..!").show();
            return;
        }

        String orderId = lblSupOrderId.getText();
        Date dateOfOrder = Date.valueOf(orderDate.getText());
        String memberId = cmbMemberId.getSelectionModel().getSelectedItem().toString();
        String cashierId = cmbCashierId.getSelectionModel().getSelectedItem().toString();
        String date = orderDate.getText();

        // CartDTO - OrderDetailsDTO
        ArrayList<CartDto> cartList = new ArrayList<>();
        for (CartTm cartTm : cartData) {
            CartDto cartDto = new CartDto(
                    orderId,
                    memberId,
                    cashierId,
                    cartTm.getSupplementId(),
                    cartTm.getTotal(),
                    cartTm.getCartQty(),
                    date
            );
            cartList.add(cartDto);
        }



        SupplementOrderDto supplementOrderDto = new SupplementOrderDto(
                orderId,
                selectedMemberId,
                dateOfOrder,
                cartList
        );

//        cartData
//        orderDetail -> {orderId, itemId, qty, price}
        try {
            boolean isSaved = supplementOrderModel.placeOrder(supplementOrderDto);

            if (isSaved) {
                refreshPage();
                new Alert(Alert.AlertType.INFORMATION, "Order saved").show();

            } else {
                new Alert(Alert.AlertType.ERROR, "Order fail..!").show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Order fail..!").show();
        }
    }
    }
