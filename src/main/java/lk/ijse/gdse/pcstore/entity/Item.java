package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
    private String itemId;
    private String categoryId;
    private String itemName;
    private double price;
    private int quantity;
    private int warranty;
}
