package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserTM {
    private String userId;
    private String employeeId;
    private String userName;
    private String password;
    private String userNic;
    private String userPhone;
    private String userEmail;
    private String userRole;
}
