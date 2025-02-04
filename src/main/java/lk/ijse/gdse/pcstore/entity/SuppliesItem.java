package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuppliesItem {
    private String suppliesId;
    private String itemId;
    private Integer quantity;
    private Double unitPrice;
}
