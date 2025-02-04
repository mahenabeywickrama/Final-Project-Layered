package lk.ijse.gdse.pcstore.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PCCartTM {
    private String itemId;
    private String itemName;
    private String category;
    private double unitPrice;
    private int cartQuantity;
    private double total;
    private Button removeBtn;
}
