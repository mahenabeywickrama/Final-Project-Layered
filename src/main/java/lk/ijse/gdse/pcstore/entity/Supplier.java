package lk.ijse.gdse.pcstore.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Supplier {
    private String supplierId;
    private String supplierName;
    private String supplierAddress;
    private String supplierNic;
    private String supplierPhone;
    private String supplierEmail;
}
