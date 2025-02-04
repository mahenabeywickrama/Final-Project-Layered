package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepairTM {
    private String repairId;
    private String customerId;
    private String description;
    private String status;
    private LocalDate receiveDate;
    private LocalDate returnDate;
}
