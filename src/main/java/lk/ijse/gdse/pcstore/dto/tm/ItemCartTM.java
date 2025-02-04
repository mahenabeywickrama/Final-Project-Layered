package lk.ijse.gdse.pcstore.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemCartTM {
    private String itemId;
    private String itemName;
    private int cartQuantity;
    private double unitPrice;
    private double total;
    private Button removeBtn;
}
