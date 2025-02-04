package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String userId;
    private String employeeId;
    private String userName;
    private String password;
    private String userNic;
    private String userPhone;
    private String userEmail;
    private String userRole;
}
