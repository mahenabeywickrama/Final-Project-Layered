package lk.ijse.gdse.pcstore.entity;

import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PC {
    private String pcId;
    private String description;
    private double totalCost;

    private ArrayList<PCItem> pcItemDTOS;
}
