package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MemberModel {

    public boolean saveMember(MemberDto memberDto) throws SQLException {
        return CrudUtil.execute("insert into member values (?,?,?,?)",
                memberDto.getMemberId(),
                memberDto.getMemberName(),
                memberDto.getMemberAddress(),
                memberDto.getMemberContact()
        );
    }
    public boolean updateMember(MemberDto memberDto) throws SQLException {
        return CrudUtil.execute("update member set MemberName=?, MemberAddress=?, MemberContact=? where MemberId=?",
                memberDto.getMemberName(),
                memberDto.getMemberAddress(),
                memberDto.getMemberContact(),
                memberDto.getMemberId()
                );
    }
    public boolean deleteMember(String memberId) throws SQLException {
        return CrudUtil.execute("delete from member where MemberId=?",
                memberId
        );
    }

    public ArrayList<MemberDto> getAllMembers() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select * from member");

        ArrayList<MemberDto> memberDtoArrayList = new ArrayList<>();
        while (resultSet.next()) {
            MemberDto memberDto = new MemberDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4)
            );
            memberDtoArrayList.add(memberDto);
        }
        return memberDtoArrayList;
    }

    public String getNextMemberId() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select MemberId from member order by MemberId desc limit 1");
        char tableCharacter ='M';
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

    public String findNameById(String selectedMemberId) throws SQLException {
        ResultSet rst = CrudUtil.execute(
                "select MemberName from member where MemberId=?",
                selectedMemberId
        );

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
    public ArrayList<String> getAllMemberIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select MemberId from member");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }
}
