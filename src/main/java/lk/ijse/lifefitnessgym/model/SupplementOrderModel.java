package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.db.DBConnection;
import lk.ijse.lifefitnessgym.dto.SupplementOrderDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SupplementOrderModel {

    private final SupplementOrderDetailsModel supplementOrderDetailsModel = new SupplementOrderDetailsModel();

    public String getNextSupplementOrderId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select OrderId from s_orders order by OrderId desc limit 1");
        char tableCharacter = 'O';
        if (resultSet.next()) {
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNUmber = lastIdNumber + 1; // 2
            String nextIdString = String.format(tableCharacter + "%03d", nextIdNUmber);
            return nextIdString;
        }
        return tableCharacter + "001";
    }

    public boolean placeOrder(SupplementOrderDto supplementOrderDto) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean isOrderSaved = CrudUtil.execute("insert into s_orders values (?,?,?)",
                    supplementOrderDto.getOrderId(),
                    supplementOrderDto.getMemberId(),
                    supplementOrderDto.getOrderDate()
            );
            if (isOrderSaved) {
                boolean isOrderDetailsSaved = supplementOrderDetailsModel.saveOrderDetailsList(supplementOrderDto.getCartList());
                if (isOrderDetailsSaved) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (Exception e) {
            connection.rollback();
            return false;
        }finally {
            connection.setAutoCommit(true);
    }
}
}
