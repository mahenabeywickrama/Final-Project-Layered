package lk.ijse.gdse.pcstore.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Login {
    private String loginId;
    private String userId;
    private LocalDate date;
    private LocalTime time;
}
