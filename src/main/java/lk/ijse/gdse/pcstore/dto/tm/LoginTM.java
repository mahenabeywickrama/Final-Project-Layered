package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginTM {
    private String loginId;
    private String userId;
    private String userName;
    private LocalDate date;
    private LocalTime time;
    private String role;
}
