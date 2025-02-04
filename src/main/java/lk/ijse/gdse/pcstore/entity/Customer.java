package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {
    private String customerId;
    private String customerName;
    private String customerAddress;
    private String customerNic;
    private String customerPhone;
    private String customerEmail;
}
