package lk.ijse.gdse.pcstore.entity;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Repair {
    private String repairId;
    private String customerId;
    private String description;
    private String status;
    private LocalDate receiveDate;
    private LocalDate returnDate;
}
