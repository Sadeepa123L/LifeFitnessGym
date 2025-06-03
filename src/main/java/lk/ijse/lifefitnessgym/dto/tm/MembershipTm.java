package lk.ijse.lifefitnessgym.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MembershipTm {
    private String membershipId;
    private String membershipType;
    private String validPeriod;
    private Double price;
}
