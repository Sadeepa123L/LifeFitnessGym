package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.CashierDto;
import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CashierModel {

    public boolean saveCashier(CashierDto cashierDto) throws SQLException {
        return CrudUtil.execute("insert into cashier values (?,?,?)",
                cashierDto.getCashierId(),
                cashierDto.getCashierName(),
                cashierDto.getContact()
        );
    }
    public boolean updateCashier(CashierDto cashierDto) throws SQLException {
        return CrudUtil.execute("update cashier set CashierName=?, CashierContact=? where CashierId=?",
                cashierDto.getCashierName(),
                cashierDto.getContact(),
                cashierDto.getCashierId()
        );
    }
    public boolean deleteCashier(String cashierId) throws SQLException {
        return CrudUtil.execute("delete from cashier where CashierId=?",
                cashierId
        );
    }
    public ArrayList<CashierDto> getAllCashier() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from cashier");

        ArrayList<CashierDto> cashierDtoArrayList = new ArrayList<>();
        while (resultSet.next()) {
            CashierDto cashierDto = new CashierDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            cashierDtoArrayList.add(cashierDto);
        }
        return cashierDtoArrayList;
    }

    public String getNextCashierId() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select CashierId from cashier order by CashierId desc limit 1");
        char tableCharacter ='C';
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

    public ArrayList<String> getAllCashierIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select CashierId from cashier");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public String findNameById(String selectedCashierId) throws SQLException {
        ResultSet rst = CrudUtil.execute(
                "select CashierName from cashier where CashierId=?",
                selectedCashierId
        );

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
