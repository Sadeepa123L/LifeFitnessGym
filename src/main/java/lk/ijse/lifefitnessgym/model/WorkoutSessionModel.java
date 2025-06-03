package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.dto.SupplementDto;
import lk.ijse.lifefitnessgym.dto.WorkoutSessionDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WorkoutSessionModel {
    public boolean saveSession(WorkoutSessionDto workoutSessionDto) throws SQLException {
        return CrudUtil.execute("insert into workout_session values (?,?,?,?,?)",
                workoutSessionDto.getSessionId(),
                workoutSessionDto.getTrainerId(),
                workoutSessionDto.getSessionName(),
                workoutSessionDto.getPaymentPerHour(),
                workoutSessionDto.getTimeTable()
        );
    }
    public boolean updateSession(WorkoutSessionDto workoutSessionDto) throws SQLException {
        return CrudUtil.execute("update workout_session set TrainerId=?, SessionName=?, PaymentPerHour=?, TimeTable=? where SessionId=?",
                workoutSessionDto.getTrainerId(),
                workoutSessionDto.getSessionName(),
                workoutSessionDto.getPaymentPerHour(),
                workoutSessionDto.getTimeTable(),
                workoutSessionDto.getSessionId()
        );
    }
    public boolean deleteSession(String sessionId) throws SQLException {
        return CrudUtil.execute("delete from workout_session where SessionId=?",
                sessionId
        );
    }

    public ArrayList<WorkoutSessionDto> getAllSession() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from workout_session");

        ArrayList<WorkoutSessionDto> sessionDtoArrayList = new ArrayList<>();
        while (resultSet.next()) {
            WorkoutSessionDto workoutSessionDto= new WorkoutSessionDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4),
                    resultSet.getString(5)
            );
            sessionDtoArrayList.add(workoutSessionDto);
        }
        return sessionDtoArrayList;
    }

    public String getNextSessionId() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select SessionId from workout_session order by SessionId desc limit 1");
        char tableCharacter ='W';
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
    public ArrayList<String> getAllSessionIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select SessionId from workout_session");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }

    public WorkoutSessionDto findById(String selectedSessionId) throws SQLException {
        ResultSet rst = CrudUtil.execute(
                "select * from workout_session where SessionId=?",
                selectedSessionId
        );

        if (rst.next()) {
            return new WorkoutSessionDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getString(5)
            );
        }
        return null;
    }
}
