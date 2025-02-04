package lk.ijse.gdse.pcstore.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashBoard {
    private String customerId;
    private String employeeId;
    private LocalTime time;
    private LocalDate date;
    private String type;
    private double total;
    private String status;
}
