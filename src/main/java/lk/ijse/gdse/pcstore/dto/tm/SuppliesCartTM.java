package lk.ijse.gdse.pcstore.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuppliesCartTM {
    private String itemId;
    private String name;
    private int quantity;
    private double unitPrice;
    private int oldQuantity;
    private int newQuantity;
    private double total;
    private Button delete;
}
