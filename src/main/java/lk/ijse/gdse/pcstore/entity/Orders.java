package lk.ijse.gdse.pcstore.entity;

import lombok.*;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Orders {
    private String orderId;
    private String customerId;
    private String employeeId;
    private Date orderDate;
    private Time orderTime;
    private String type;
    private double orderAmount;
    private String orderStatus;

    private ArrayList<OrdersItem> ordersItems;
    private ArrayList<OrdersRepair> ordersRepairs;
}
