package lk.ijse.gdse.pcstore.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerDTO {
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerNic;
    private String customerPhone;
    private String customerEmail;
}
