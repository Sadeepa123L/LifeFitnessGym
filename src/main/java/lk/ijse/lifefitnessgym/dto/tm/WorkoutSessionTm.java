package lk.ijse.lifefitnessgym.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class WorkoutSessionTm {
    private String sessionId;
    private String trainerId;
    private String sessionName;
    private Double paymentPerHour;
    private String timeTable;
}
