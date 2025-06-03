package lk.ijse.lifefitnessgym.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CartDto {
    private String orderId;
    private String memberId;
    private String CashierId;
    private String supplementId;
    private Double total;
    private int quantity;
    private String date;

}
