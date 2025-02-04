package lk.ijse.gdse.pcstore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PCItemDTO {
    private String pcId;
    private String itemId;
    private int qty;
    private double price;
}
