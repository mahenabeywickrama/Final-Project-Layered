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
public class OrdersDTO {
    private String orderId;
    private String customerId;
    private String employeeId;
    private Date orderDate;
    private Time orderTime;
    private String type;
    private double orderAmount;
    private String orderStatus;

    private ArrayList<OrdersItemDTO> ordersItemDTOS;
    private ArrayList<OrdersRepairDTO> ordersRepairDTOS;
}
