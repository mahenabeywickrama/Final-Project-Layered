package lk.ijse.gdse.pcstore.dto;

import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SuppliesDTO {
    private String suppliesId;
    private Date date;
    private Time time;
    private String supplierId;
    private ArrayList<SuppliesItemDTO> suppliesItemDTOS;
}
