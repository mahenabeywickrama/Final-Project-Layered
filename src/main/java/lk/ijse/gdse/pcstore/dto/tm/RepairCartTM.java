package lk.ijse.gdse.pcstore.dto.tm;

import javafx.scene.control.Button;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RepairCartTM {
    private String repairId;
    private String description;
    private double repairPrice;
    private Button removeBtn;
}
