package lk.ijse.gdse.pcstore.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginDTO {
    private String loginId;
    private String userId;
    private LocalDate date;
    private LocalTime time;
}
