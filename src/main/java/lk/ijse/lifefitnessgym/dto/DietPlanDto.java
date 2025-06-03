package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString

public class DietPlanDto {
    private String dietId;
    private String dietName;
    private String purpose;
    private Double price;
}
