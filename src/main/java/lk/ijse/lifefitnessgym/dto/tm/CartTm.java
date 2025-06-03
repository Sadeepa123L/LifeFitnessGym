package lk.ijse.lifefitnessgym.dto.tm;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lk.ijse.lifefitnessgym.controller.SupplementOrderController;
import lk.ijse.lifefitnessgym.dto.SupplementOrderDto;
import lombok.*;

import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class CartTm {
    private String supplementId;
    private String supplementName;
    private int cartQty;
    private Double price;
    private Double total;
    private Button btnRemove;

}

