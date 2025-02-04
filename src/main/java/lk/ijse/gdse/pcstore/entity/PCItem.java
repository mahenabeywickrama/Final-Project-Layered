package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PCItem {
    private String pcId;
    private String itemId;
    private int qty;
    private double price;
}
