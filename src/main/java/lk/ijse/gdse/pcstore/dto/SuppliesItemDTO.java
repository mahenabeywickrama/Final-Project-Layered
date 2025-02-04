package lk.ijse.gdse.pcstore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuppliesItemDTO {
    private String suppliesId;
    private String itemId;
    private Integer quantity;
    private Double unitPrice;
}
