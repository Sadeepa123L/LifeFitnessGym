package lk.ijse.lifefitnessgym.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class DietPlanOrderTm {
    String paymentIdD;
    String memberId;
    String planId;
    String date;
    Double price;
}
