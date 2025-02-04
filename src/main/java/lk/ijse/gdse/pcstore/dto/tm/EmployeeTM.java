package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class EmployeeTM {
    private String employeeId;
    private String employeeName;
    private String employeeAddress;
    private String employeeNic;
    private String employeePhone;
    private String employeeRole;
}
