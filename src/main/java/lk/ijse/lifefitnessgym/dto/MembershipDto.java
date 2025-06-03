package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MembershipDto {
    private String membershipId;
    private String membershipType;
    private String validPeriod;
    private Double price;
}
