package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.DietPlanOrderDto;
import lk.ijse.lifefitnessgym.dto.SubscriptionDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SubscriptionModel {
    public boolean placeOrder(SubscriptionDto subscriptionDto) throws SQLException {
        return CrudUtil.execute("insert into subscription values (?,?,?,?,?,?)",
                subscriptionDto.getOrderId(),
                subscriptionDto.getMemberId(),
                subscriptionDto.getMembershipId(),
                subscriptionDto.getPrice(),
                subscriptionDto.getDate(),
                subscriptionDto.getEndDate()
        );
    }

    public String getNextOrderId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select PaymentIdM from subscription order by PaymentIdM desc limit 1");
        char tableCharacter ='O';
        if(resultSet.next()){
            String lastId = resultSet.getString(1);
            String lastIdNumberString = lastId.substring(1);
            int lastIdNumber = Integer.parseInt(lastIdNumberString);
            int nextIdNUmber = lastIdNumber + 1; // 2
            String nextIdString = String.format(tableCharacter + "%03d", nextIdNUmber);
            return nextIdString;
        }
        return tableCharacter + "001";
    }
    public ArrayList<SubscriptionDto> getAllSubscriptionOrder() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from subscription ");

        ArrayList<SubscriptionDto> subscriptionDtoArrayList = new ArrayList<>();
        while(resultSet.next()){
            SubscriptionDto subscriptionDto = new SubscriptionDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            );
            subscriptionDtoArrayList.add(subscriptionDto);
        }
        return subscriptionDtoArrayList;
    }
}
