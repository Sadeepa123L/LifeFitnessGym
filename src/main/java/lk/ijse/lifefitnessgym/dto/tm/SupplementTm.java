package lk.ijse.lifefitnessgym.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SupplementTm {
    private String supplementId;
    private String supplementType;
    private String supplementName;
    private String qty;
    private double price;
}
