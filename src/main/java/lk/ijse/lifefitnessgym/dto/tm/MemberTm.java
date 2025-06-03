package lk.ijse.lifefitnessgym.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class MemberTm {
    private String memberId;
    private String memberName;
    private String memberAddress;
    private String memberContact;
}
