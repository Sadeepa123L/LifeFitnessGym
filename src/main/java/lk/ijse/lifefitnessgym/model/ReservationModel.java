package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.DietPlanOrderDto;
import lk.ijse.lifefitnessgym.dto.ReservationDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ReservationModel {
    public String getNextOrderId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select PaymentIdW from reservation order by PaymentIdW desc limit 1");
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
    public ArrayList<ReservationDto> getAllReservations() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from reservation");

        ArrayList<ReservationDto> reservationDtoArrayList = new ArrayList<>();
        while(resultSet.next()){
            ReservationDto reservationDto = new ReservationDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getString(6)
            );
            reservationDtoArrayList.add(reservationDto);
        }
        return reservationDtoArrayList;
    }
    public boolean placeOrder(ReservationDto reservationDto) throws SQLException {
        return CrudUtil.execute("insert into reservation values (?,?,?,?,?,?)",
                reservationDto.getOrderId(),
                reservationDto.getMemberId(),
                reservationDto.getSessionId(),
                reservationDto.getTime(),
                reservationDto.getPrice(),
                reservationDto.getDate()
        );
    }
}
