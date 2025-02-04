package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrdersRepair {
    private String orderId;
    private String repairId;
    private double repairPrice;
}
