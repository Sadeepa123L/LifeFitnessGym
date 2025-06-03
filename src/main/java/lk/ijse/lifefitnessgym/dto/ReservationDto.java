package lk.ijse.lifefitnessgym.dto;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ReservationDto {
    private String orderId;
    private String memberId;
    private String sessionId;
    private String time;
    private Double price;
    private String date;
}
