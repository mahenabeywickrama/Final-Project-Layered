package lk.ijse.gdse.pcstore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeDTO {
    private String employeeId;
    private String employeeName;
    private String employeeAddress;
    private String employeeNic;
    private String employeePhone;
    private String employeeRole;
}
