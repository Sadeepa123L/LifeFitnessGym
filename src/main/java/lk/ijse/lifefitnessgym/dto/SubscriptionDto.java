package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SubscriptionDto {
    private String orderId;
    private String memberId;
    private String membershipId;
    private Double price;
    private String date;
    private String endDate;
}
