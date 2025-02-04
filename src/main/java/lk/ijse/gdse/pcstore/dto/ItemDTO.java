package lk.ijse.gdse.pcstore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ItemDTO {
    private String itemId;
    private String categoryId;
    private String itemName;
    private double price;
    private int quantity;
    private int warranty;
}
