package lk.ijse.lifefitnessgym.dto.tm;
import lombok.*;

import java.awt.dnd.DropTarget;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ReservationTm {
    private String orderId;
    private String memberId;
    private String sessionId;
    private int time;
    private Double price;
    private String date;
}
