package lk.ijse.lifefitnessgym.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SubscriptionTm {
    private String orderId;
    private String memberId;
    private String membershipId;
    private Double price;
    private String date;
    private String endDate;
}
