package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.OrdersDAO;
import lk.ijse.gdse.pcstore.entity.Orders;
import lk.ijse.gdse.pcstore.entity.OrdersItem;
import lk.ijse.gdse.pcstore.entity.OrdersRepair;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class OrdersDAOImpl implements OrdersDAO {
    @Override
    public String getNextId() throws SQLException {
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

    @Override
    public ArrayList<Orders> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from orders");

        ArrayList<Orders> orders = new ArrayList<>();

        while (rst.next()) {
            Orders order = new Orders(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    Date.valueOf(rst.getString(4)),
                    Time.valueOf(rst.getString(5)),
                    rst.getString(6),
                    Double.parseDouble(rst.getString(7)),
                    rst.getString(8),
                    getAllOrderItems(rst.getString(1)),
                    getAllOrderRepairs(rst.getString(1))
            );
            orders.add(order);
        }
        return orders;
    }

    @Override
    public boolean save(Orders dto) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Orders dto) throws SQLException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return false;
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return null;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return null;
    }

    @Override
    public Orders findById(String id) throws SQLException {
        return null;
    }

    @Override
    public Orders findByName(String name) throws SQLException {
        return null;
    }

    @Override
    public ArrayList<OrdersItem> getAllOrderItems(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from orders_item where orders_id = ?", id);
        ArrayList<OrdersItem> ordersItems = new ArrayList<>();
        while (rst.next()) {
            OrdersItem ordersItem = new OrdersItem(
                    rst.getString(1),
                    rst.getString(2),
                    Integer.parseInt(rst.getString(3)),
                    Double.parseDouble(rst.getString(4))
            );
            ordersItems.add(ordersItem);
        }

        return ordersItems;
    }

    @Override
    public ArrayList<OrdersRepair> getAllOrderRepairs(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from orders_repair where orders_id = ?", id);
        ArrayList<OrdersRepair> ordersRepairs = new ArrayList<>();
        while (rst.next()) {
            OrdersRepair ordersRepair = new OrdersRepair(
                    rst.getString(1),
                    rst.getString(2),
                    Double.parseDouble(rst.getString(3))
            );
            ordersRepairs.add(ordersRepair);
        }

        return ordersRepairs;
    }

    @Override
    public ArrayList<String> getAllOrdersIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select orders_id from orders where status='PENDING'");

        ArrayList<String> ordersIds = new ArrayList<>();

        while (rst.next()) {
            ordersIds.add(rst.getString(1));
        }

        return ordersIds;
    }

    @Override
    public ArrayList<String> getAllOrdersIdsPaid() throws SQLException {
        ResultSet rst = SQLUtil.execute("select orders_id from orders where status='PAID'");

        ArrayList<String> ordersIds = new ArrayList<>();

        while (rst.next()) {
            ordersIds.add(rst.getString(1));
        }

        return ordersIds;
    }

    @Override
    public String findOrdersCustomer(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select customer_id from orders where orders_id = ?", selectedOrdersId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    @Override
    public String findOrdersEmployee(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select employee_id from orders where orders_id = ?", selectedOrdersId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    @Override
    public String findOrdersPrice(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select total_amount from orders where orders_id = ?", selectedOrdersId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    @Override
    public String findOrdersDate(String selectedOrdersId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select date from orders where orders_id = ?", selectedOrdersId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    @Override
    public boolean updateOrdersPaid(String ordersId) throws SQLException {
        return SQLUtil.execute("update orders set status = 'PAID' where orders_id = ?", ordersId);
    }

    @Override
    public boolean updateOrdersPending(String ordersId) throws SQLException {
        return SQLUtil.execute("update orders set status = 'PENDING' where orders_id = ?", ordersId);
    }
}
