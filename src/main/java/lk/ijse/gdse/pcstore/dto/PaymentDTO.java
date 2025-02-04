package lk.ijse.gdse.pcstore.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PaymentDTO {
    private String paymentId;
    private String ordersId;
    private LocalDate paymentDate;
    private LocalTime paymentTime;
    private String paymentMethod;
    private double paymentAmount;
    private double balance;
}
