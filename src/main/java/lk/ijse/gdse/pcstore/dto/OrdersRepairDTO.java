package lk.ijse.gdse.pcstore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersRepairDTO {
    private String orderId;
    private String repairId;
    private double repairPrice;
}
