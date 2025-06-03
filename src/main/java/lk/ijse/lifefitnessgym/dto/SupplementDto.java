package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SupplementDto {
    private String supplementId;
    private String supplementType;
    private String supplementName;
    private String qty;
    private double price;
}
