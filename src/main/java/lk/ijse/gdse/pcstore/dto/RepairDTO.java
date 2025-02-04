package lk.ijse.gdse.pcstore.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepairDTO {
    private String repairId;
    private String customerId;
    private String description;
    private String status;
    private LocalDate receiveDate;
    private LocalDate returnDate;
}
