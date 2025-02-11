package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.db.DBConnection;
import lk.ijse.gdse.pcstore.dto.DashBoardDTO;
import lk.ijse.gdse.pcstore.dto.OrdersDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class OrdersModel {
    private final OrdersItemModel ordersItemModel = new OrdersItemModel();
    private final OrdersRepairModel ordersRepairModel = new OrdersRepairModel();

    public String getNextOrderId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select orders_id from orders order by orders_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("O%03d", newIdIndex);
        }
        return "O001";
    }

    public boolean saveOrder(OrdersDTO ordersDTO) throws SQLException {
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            boolean isOrderSaved = SQLUtil.execute(
                    "insert into orders values (?,?,?,?,?,?,?,?)",
                    ordersDTO.getOrderId(),
                    ordersDTO.getCustomerId(),
                    ordersDTO.getEmployeeId(),
                    ordersDTO.getOrderDate(),
                    ordersDTO.getOrderTime(),
                    ordersDTO.getType(),
                    ordersDTO.getOrderAmount(),
                    ordersDTO.getOrderStatus()
            );
            if (isOrderSaved) {
                boolean isOrderItemDetailListSaved = ordersItemModel.saveOrderDetailsList(ordersDTO.getOrdersItemDTOS());
                boolean isOrderRepairDetailsListSaved = ordersRepairModel.addDetails(ordersDTO.getOrdersRepairDTOS());

                if (isOrderItemDetailListSaved && isOrderRepairDetailsListSaved) {
                    connection.commit();
                    return true;
                }
            }
            connection.rollback();
            return false;
        } catch (SQLException e) {
            connection.rollback();
            System.out.println(e.getMessage());
            return false;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    public ArrayList<DashBoardDTO> getAllOrders() throws SQLException {
        ResultSet rst = SQLUtil.execute("select customer_id, employee_id, time, date, type, total_amount, status from orders");

        ArrayList<DashBoardDTO> dashBoardDTOS = new ArrayList<>();

        while (rst.next()) {
            DashBoardDTO dashBoardDTO = new DashBoardDTO(
                    rst.getString(1),
                    rst.getString(2),
                    LocalTime.parse(rst.getString(3)),
                    LocalDate.parse(rst.getString(4)),
                    rst.getString(5),
                    Double.parseDouble(rst.getString(6)),
                    rst.getString(7)
            );
            dashBoardDTOS.add(dashBoardDTO);
        }
        return dashBoardDTOS;
    }

    public ArrayList<String> getAllOrdersIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select orders_id from orders where status='PENDING'");

        ArrayList<String> ordersIds = new ArrayList<>();

        while (rst.next()) {
            ordersIds.add(rst.getString(1));
        }

        return ordersIds;
    }

    public ArrayList<String> getAllOrdersIdsPaid() throws SQLException {
        ResultSet rst = SQLUtil.execute("select orders_id from orders where status='PAID'");

        ArrayList<String> ordersIds = new ArrayList<>();

        while (rst.next()) {
            ordersIds.add(rst.getString(1));
        }

        return ordersIds;
    }

    public String findOrdersCustomer(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select customer_id from orders where orders_id = ?", selectedOrdersId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public String findOrdersEmployee(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select employee_id from orders where orders_id = ?", selectedOrdersId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public String findOrdersPrice(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select total_amount from orders where orders_id = ?", selectedOrdersId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public String findOrdersDate(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select date from orders where orders_id = ?", selectedOrdersId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public boolean updateOrdersPaid(String ordersId) throws SQLException {
        return SQLUtil.execute("update orders set status = 'PAID' where orders_id = ?", ordersId);
    }

    public boolean updateOrdersPending(String ordersId) throws SQLException {
        return SQLUtil.execute("update orders set status = 'PENDING' where orders_id = ?", ordersId);
    }

    public ArrayList<OrdersDTO> getAllOrderDTOs() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from orders");

        ArrayList<OrdersDTO> ordersDTODS = new ArrayList<>();

        while (rst.next()) {
            OrdersDTO ordersDTO = new OrdersDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    Date.valueOf(rst.getString(4)),
                    Time.valueOf(rst.getString(5)),
                    rst.getString(6),
                    Double.parseDouble(rst.getString(7)),
                    rst.getString(8),
                    getAllOrderItemDTOs(rst.getString(1)),
                    getAllOrderRepairDTOs(rst.getString(1))
            );
            ordersDTODS.add(ordersDTO);
        }
        return ordersDTODS;
    }

    public ArrayList<OrdersItemDTO> getAllOrderItemDTOs(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from orders_item where orders_id = ?", id);
        ArrayList<OrdersItemDTO> ordersItemDTOS = new ArrayList<>();
        while (rst.next()) {
            OrdersItemDTO ordersItemDTO = new OrdersItemDTO(
                    rst.getString(1),
                    rst.getString(2),
                    Integer.parseInt(rst.getString(3)),
                    Double.parseDouble(rst.getString(4))
            );
            ordersItemDTOS.add(ordersItemDTO);
        }

        return ordersItemDTOS;
    }

    public ArrayList<OrdersRepairDTO> getAllOrderRepairDTOs(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from orders_repair where orders_id = ?", id);
        ArrayList<OrdersRepairDTO> ordersRepairDTOS = new ArrayList<>();
        while (rst.next()) {
            OrdersRepairDTO ordersRepairDTO = new OrdersRepairDTO(
                    rst.getString(1),
                    rst.getString(2),
                    Double.parseDouble(rst.getString(3))
            );
            ordersRepairDTOS.add(ordersRepairDTO);
        }

        return ordersRepairDTOS;
    }

}
