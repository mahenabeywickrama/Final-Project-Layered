package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemTM {
    private String itemId;
    private String categoryId;
    private String itemName;
    private double price;
    private int quantity;
    private int warranty;
}
