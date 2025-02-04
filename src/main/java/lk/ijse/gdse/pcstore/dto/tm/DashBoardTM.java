package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DashBoardTM {
    private String customer;
    private String employee;
    private LocalTime time;
    private String type;
    private double total;
    private String status;
}
