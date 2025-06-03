package lk.ijse.lifefitnessgym.dto;


import lombok.*;

import java.sql.Date;
import java.util.ArrayList;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class SupplementOrderDto {
    private String orderId;
    private String memberId;
    private Date orderDate;
    private ArrayList<CartDto> cartList;
}
