package lk.ijse.gdse.pcstore.dto.tm;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SupplierTM {
    private String supplierId;
    private String supplierName;
    private String supplierAddress;
    private String supplierNic;
    private String supplierPhone;
    private String supplierEmail;
}
