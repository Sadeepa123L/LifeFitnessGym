package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.MemberDto;
import lk.ijse.lifefitnessgym.dto.TrainerDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class TrainerModel {

    public boolean saveTrainer(TrainerDto trainerDto) throws SQLException {
        return CrudUtil.execute("insert into trainer values (?,?,?)",
                trainerDto.getTrainerId(),
                trainerDto.getTrainerName(),
                trainerDto.getTrainerContact()
        );
    }

    public boolean updateTrainer(TrainerDto trainerDto) throws SQLException {
        return CrudUtil.execute("update trainer set TrainerName=?, TrainerContact=? where TrainerId=?",
                trainerDto.getTrainerName(),
                trainerDto.getTrainerContact(),
                trainerDto.getTrainerId()
        );
    }

    public boolean deleteTrainer(String trainerId) throws SQLException {
        return CrudUtil.execute("delete from trainer where TrainerId=?",
                trainerId
        );
    }

    public ArrayList<TrainerDto> getAllTrainers() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from trainer");

        ArrayList<TrainerDto> trainerDtoArrayList = new ArrayList<>();
        while (resultSet.next()) {
            TrainerDto trainerDto = new TrainerDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
            trainerDtoArrayList.add(trainerDto);
        }
        return trainerDtoArrayList;
    }

    public String getNextTrainerId() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select TrainerId from trainer order by TrainerId desc limit 1");
        char tableCharacter ='T';
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
    public ArrayList<String> getAllTrainerIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select TrainerId from trainer");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }
    public String findNameById(String selectedTrainerId) throws SQLException {
        ResultSet rst = CrudUtil.execute(
                "select TrainerName from trainer where TrainerId=?",
                selectedTrainerId
        );

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
