package lk.ijse.lifefitnessgym.dto.tm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SupplementOrderTm {
    private String orderId;
    private String memberId;
    private String CashierId;
    private String supplementId;
    private Double total;
    private int quantity;
    private String date;
}
