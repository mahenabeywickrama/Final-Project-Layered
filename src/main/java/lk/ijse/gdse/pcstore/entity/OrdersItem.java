package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersItem {
    private String orderId;
    private String itemId;
    private int quantity;
    private double price;
}
