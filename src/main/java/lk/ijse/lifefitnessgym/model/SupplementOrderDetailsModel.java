package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.CartDto;
import lk.ijse.lifefitnessgym.dto.DietPlanOrderDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class SupplementOrderDetailsModel {
    private final SupplementModel supplementModel = new SupplementModel();

    public boolean saveOrderDetailsList(ArrayList<CartDto> cartList) throws Exception {
        for (CartDto cartDto : cartList) {
            boolean isOrderDetailsSaved = saveOrderDetail(cartDto);
            if (!isOrderDetailsSaved) {
                return false;
            }

            boolean isSupplementUpdate = supplementModel.reduceQty(cartDto);
            if (!isSupplementUpdate) {
                return false;
            }
        }
        return true;
    }

    private boolean saveOrderDetail(CartDto cartDto) throws SQLException {
        return CrudUtil.execute(
                "insert into supplement_order values (?,?,?,?,?,?,?)",
                cartDto.getOrderId(),
                cartDto.getMemberId(),
                cartDto.getCashierId(),
                cartDto.getSupplementId(),
                cartDto.getTotal(),
                cartDto.getQuantity(),
                cartDto.getDate()
        );
    }

    public ArrayList<CartDto> getAllSupplementOrders() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from supplement_order");

        ArrayList<CartDto> supplementOrderDtoList = new ArrayList<>();
        while(resultSet.next()){
            CartDto cartDto = new CartDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getInt(6),
                    resultSet.getString(7)
            );
            supplementOrderDtoList.add(cartDto);
        }
        return supplementOrderDtoList;
    }
}
