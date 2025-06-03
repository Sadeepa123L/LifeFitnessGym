package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class DietPlanOrderDto {
    private String paymentIdD;
    private String memberId;
    private String planId;
    private String date;
    private Double price;
}
