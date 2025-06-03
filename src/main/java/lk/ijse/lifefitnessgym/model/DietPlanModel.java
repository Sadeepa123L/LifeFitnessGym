package lk.ijse.lifefitnessgym.model;

import lk.ijse.lifefitnessgym.dto.DietPlanDto;
import lk.ijse.lifefitnessgym.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DietPlanModel {
    public boolean saveDietPlan(DietPlanDto dietPlanDto) throws SQLException {
        return CrudUtil.execute("insert into diet_plan values (?,?,?,?)",
                    dietPlanDto.getDietId(),
                    dietPlanDto.getDietName(),
                    dietPlanDto.getPurpose(),
                    dietPlanDto.getPrice()
                );
    }
    public boolean updateDietPlan(DietPlanDto dietPlanDto) throws SQLException {
        return CrudUtil.execute("update diet_plan set DietName=?, purpose=?, Price=? where DietId=?",
                dietPlanDto.getDietName(),
                dietPlanDto.getPurpose(),
                dietPlanDto.getPrice(),
                dietPlanDto.getDietId()
        );
    }

    public boolean deleteDietPlan(String dietId) throws SQLException {
        return CrudUtil.execute("delete from diet_plan where DietId=?",
                dietId
        );
    }
    public ArrayList<DietPlanDto> getAllDietPlan() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("select * from diet_plan");

        ArrayList<DietPlanDto> dietPlanDtoArrayList = new ArrayList<>();
        while(resultSet.next()){
            DietPlanDto dietPlanDto = new DietPlanDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDouble(4)
            );
            dietPlanDtoArrayList.add(dietPlanDto);
        }
        return dietPlanDtoArrayList;
    }
    public String getNextDietId() throws SQLException{
        ResultSet resultSet = CrudUtil.execute("select DietId from diet_plan order by DietId desc limit 1");
        char tableCharacter ='D';
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
    public ArrayList<String> getAllPlanIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select DietId from diet_plan");
        ArrayList<String> list = new ArrayList<>();
        while (rst.next()) {
            String id = rst.getString(1);
            list.add(id);
        }
        return list;
    }
    public DietPlanDto findById(String selectedDietId) throws SQLException {
        ResultSet rst = CrudUtil.execute(
                "select * from diet_plan where DietId=?",
                selectedDietId
        );
        if (rst.next()) {
            return new DietPlanDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            );
        }
        return null;
    }
}
