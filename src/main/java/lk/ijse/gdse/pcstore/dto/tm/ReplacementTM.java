package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplacementTM {
    private String replacementId;
    private String ordersId;
    private String faultyItemId;
    private String replacedItemId;
    private int qty;
    private LocalDate date;
    private LocalTime time;
    private String remarks;
}
