package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.DietPlanDto;
import lk.ijse.lifefitnessgym.dto.DietPlanOrderDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DietPlanOrderModel {
    public boolean placeOrder(DietPlanOrderDto dietPlanOrderDto) throws SQLException {
        return CrudUtil.execute("insert into diet_plan_order values (?,?,?,?,?)",
                dietPlanOrderDto.getPaymentIdD(),
                dietPlanOrderDto.getMemberId(),
                dietPlanOrderDto.getPlanId(),
                dietPlanOrderDto.getDate(),
                dietPlanOrderDto.getPrice()
                );
    }
    public String getNextOrderId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select PaymentIdD from diet_plan_order order by PaymentIdD desc limit 1");
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

    public ArrayList<DietPlanOrderDto> getAllDietPlanOrder() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from diet_plan_order");

        ArrayList<DietPlanOrderDto> dietPlanOrderDtoArrayList = new ArrayList<>();
        while(resultSet.next()){
            DietPlanOrderDto dietPlanOrderDto = new DietPlanOrderDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)
            );
            dietPlanOrderDtoArrayList.add(dietPlanOrderDto);
        }
        return dietPlanOrderDtoArrayList;
    }
}
