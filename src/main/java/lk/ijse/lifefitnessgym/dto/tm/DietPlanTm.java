package lk.ijse.lifefitnessgym.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class DietPlanTm {
    private String dietId;
    private String dietName;
    private String purpose;
    private Double price;
}
