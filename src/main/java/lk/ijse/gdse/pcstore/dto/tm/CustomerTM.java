package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomerTM {
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerNic;
    private String customerPhone;
    private String customerEmail;
}
