package lk.ijse.gdse.pcstore.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Replacement {
    private String replacementId;
    private String ordersId;
    private String faultyItemId;
    private String replacedItemId;
    private int qty;
    private LocalDate date;
    private LocalTime time;
    private String remarks;
}
