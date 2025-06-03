package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
public class UserDto {
    private String userId;
    private String userName;
    private String password;
    private String email;
}
