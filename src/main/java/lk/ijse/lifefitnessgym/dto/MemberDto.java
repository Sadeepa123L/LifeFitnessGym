package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MemberDto {
    private String memberId;
    private String memberName;
    private String memberAddress;
    private String memberContact;
}
