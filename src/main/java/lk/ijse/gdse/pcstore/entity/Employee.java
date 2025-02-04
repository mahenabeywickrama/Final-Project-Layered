package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {
    private String employeeId;
    private String employeeName;
    private String employeeAddress;
    private String employeeNic;
    private String employeePhone;
    private String employeeRole;
}
