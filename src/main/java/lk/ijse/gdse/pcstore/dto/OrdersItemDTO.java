package lk.ijse.gdse.pcstore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersItemDTO {
    private String orderId;
    private String itemId;
    private int quantity;
    private double price;
}
