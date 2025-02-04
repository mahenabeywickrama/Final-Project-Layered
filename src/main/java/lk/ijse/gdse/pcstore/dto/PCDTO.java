package lk.ijse.gdse.pcstore.dto;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PCDTO {
    private String pcId;
    private String description;
    private double totalCost;

    private ArrayList<PCItemDTO> pcItemDTOS;
}
