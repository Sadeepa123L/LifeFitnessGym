package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.CartDto;
import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.dto.SupplementDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplementModel {

    public boolean saveSupplement(SupplementDto supplementDto) throws SQLException {
        return CrudUtil.execute("insert into supplement values (?,?,?,?,?)",
                supplementDto.getSupplementId(),
                supplementDto.getSupplementType(),
                supplementDto.getSupplementName(),
                supplementDto.getQty(),
                supplementDto.getPrice()
        );
    }

    public boolean updateSupplement(SupplementDto supplementDto) throws SQLException {
        return CrudUtil.execute("update supplement set SupplementType=?, SupplementName=?, Quantity=?, price=? where SupplementId=?",
                supplementDto.getSupplementType(),
                supplementDto.getSupplementName(),
                supplementDto.getQty(),
                supplementDto.getPrice(),
                supplementDto.getSupplementId()
        );
    }
    public boolean deleteSupplement(String supplementId) throws SQLException {
        return CrudUtil.execute("delete from supplement where SupplementId=?",
                supplementId
        );
    }
    public ArrayList<SupplementDto> getAllSupplement() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from supplement");
        ArrayList<SupplementDto> supplementDtoArrayList = new ArrayList<>();
        while(resultSet.next()){
            SupplementDto supplementDto = new SupplementDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5)
            );
            supplementDtoArrayList.add(supplementDto);
        }
        return supplementDtoArrayList;
    }
    public String getNextSupplementId() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select SupplementId from supplement order by SupplementId desc limit 1");
        char tableCharacter ='S';
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

    public SupplementDto findById(String selectedSupplementId) throws SQLException {
        ResultSet rst = CrudUtil.execute(
                "select * from supplement where SupplementId=?",
                selectedSupplementId
        );

        if (rst.next()) {
            return new SupplementDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getDouble(5)
            );
        }
        return null;
    }
    public ArrayList<String> getAllSupplementIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select SupplementId from supplement");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }
    public boolean reduceQty(CartDto cartDto) throws SQLException {
        return CrudUtil.execute(
                "update supplement set Quantity = Quantity - ? where SupplementId = ?",
                cartDto.getQuantity(),
                cartDto.getSupplementId()
        );
    }
}
