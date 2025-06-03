package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class WorkoutSessionDto {
    private String sessionId;
    private String trainerId;
    private String sessionName;
    private Double paymentPerHour;
    private String timeTable;
}
