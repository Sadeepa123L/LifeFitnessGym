package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.dto.UserDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpPageModel {
    public boolean saveMember(UserDto userDto) throws SQLException {
        return CrudUtil.execute("insert into user values (?,?,?,?)",
                userDto.getUserId(),
                userDto.getUserName(),
                userDto.getPassword(),
                userDto.getEmail()
        );
    }

    public String getNextUserId() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select UserID from user order by UserID desc limit 1");
        char tableCharacter ='U';
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
}
