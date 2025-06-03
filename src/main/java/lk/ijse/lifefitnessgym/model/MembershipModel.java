package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.MembershipDto;
import lk.ijse.lifefitnessgym.dto.SubscriptionDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MembershipModel {
    public boolean saveMembership(MembershipDto membershipDto) throws SQLException {
        return CrudUtil.execute("insert into membership values (?,?,?,?)",
                membershipDto.getMembershipId(),
                membershipDto.getMembershipType(),
                membershipDto.getValidPeriod(),
                membershipDto.getPrice()
                );
    }
    public boolean updateMembership(MembershipDto membershipDto) throws SQLException {
        return CrudUtil.execute("update membership set MembershipType=? , ValidPeriod=?, Price=? where MembershipId=?",
                membershipDto.getMembershipType(),
                membershipDto.getValidPeriod(),
                membershipDto.getPrice(),
                membershipDto.getMembershipId()
                );
    }
    public boolean deleteMembership(String membershipId) throws SQLException {
        return CrudUtil.execute("delete from membership where MembershipId=?",
                membershipId
        );
    }

    public ArrayList<MembershipDto> getAllMembership() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from membership");

        ArrayList<MembershipDto> membershipDtoArrayList = new ArrayList<>();
        while(resultSet.next()){
            MembershipDto membershipDto = new MembershipDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            );
            membershipDtoArrayList.add(membershipDto);
        }
        return membershipDtoArrayList;
    }
    public String getNextMembershipId() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select MembershipId from membership order by MembershipId desc limit 1");
        char tableCharacter ='m';
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
    public ArrayList<String> getAllMembershipIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select MembershipId from membership");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }
    public MembershipDto findById(String selectedMembershipId) throws SQLException {
        ResultSet rst = CrudUtil.execute(
                "select * from membership where MembershipId=?",
                selectedMembershipId
        );
        if (rst.next()) {
            return new MembershipDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            );
        }
        return null;
    }
}
