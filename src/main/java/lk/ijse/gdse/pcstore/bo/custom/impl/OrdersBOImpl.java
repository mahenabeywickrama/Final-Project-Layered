package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.BOFactory;
import lk.ijse.gdse.pcstore.bo.custom.*;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.ItemDAO;
import lk.ijse.gdse.pcstore.dao.custom.OrdersDAO;
import lk.ijse.gdse.pcstore.dao.custom.OrdersItemDAO;
import lk.ijse.gdse.pcstore.db.DBConnection;
import lk.ijse.gdse.pcstore.dto.OrdersDTO;
import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;
import lk.ijse.gdse.pcstore.dto.OrdersRepairDTO;
import lk.ijse.gdse.pcstore.entity.Orders;
import lk.ijse.gdse.pcstore.entity.OrdersItem;
import lk.ijse.gdse.pcstore.entity.OrdersRepair;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrdersBOImpl implements OrdersBO {

    OrdersDAO ordersDAO = (OrdersDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDERS);
    OrdersItemDAO ordersItemDAO = (OrdersItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ORDERS_ITEM);
    ItemBO itemBO = (ItemBO) BOFactory.getInstance().getBO(BOFactory.BOType.ITEM);
    RepairBO repairBO = (RepairBO) BOFactory.getInstance().getBO(BOFactory.BOType.REPAIR);

    @Override
    public String getNextOrderId() throws SQLException {
        return ordersDAO.getNextId();
    }

    @Override
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
                boolean isOrderItemDetailListSaved = saveOrderItemDetailsList(ordersDTO.getOrdersItemDTOS());
                boolean isOrderRepairDetailsListSaved = addRepairDetails(ordersDTO.getOrdersRepairDTOS());

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

    @Override
    public boolean saveOrderItemDetailsList(ArrayList<OrdersItemDTO> ordersItemDTOS) throws SQLException {
        for (OrdersItemDTO ordersItemDTO : ordersItemDTOS) {
            boolean isOrderDetailsSaved = saveOrderItemDetail(ordersItemDTO);
            if (!isOrderDetailsSaved) {
                return false;
            }

            boolean isItemUpdated = itemBO.reduceQty(ordersItemDTO);
            if (!isItemUpdated) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveOrderItemDetail(OrdersItemDTO ordersItemDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into orders_item values (?,?,?,?)",
                ordersItemDTO.getOrderId(),
                ordersItemDTO.getItemId(),
                ordersItemDTO.getQuantity(),
                ordersItemDTO.getPrice()
        );
    }

    @Override
    public ArrayList<String> getAllItemsFromOrders(String selectedOrdersId) throws SQLException {
        return ordersItemDAO.getAllItemsFromOrders(selectedOrdersId);
    }

    @Override
    public String findQty(String selectedItemId) throws SQLException {
        return ordersItemDAO.findQty(selectedItemId);
    }

    @Override
    public boolean addRepairDetails(ArrayList<OrdersRepairDTO> ordersRepairDTOS) throws SQLException {
        for (OrdersRepairDTO ordersRepairDTO : ordersRepairDTOS) {
            boolean isRepairDetailsSaved = saveRepairDetails(ordersRepairDTO);
            if(!isRepairDetailsSaved) {
                return false;
            }

            boolean isRepairUpdated = repairBO.updateRepairForOrders(ordersRepairDTO);
            if(!isRepairUpdated) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean saveRepairDetails(OrdersRepairDTO ordersRepairDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into orders_repair values(?,?,?)",
                ordersRepairDTO.getOrderId(),
                ordersRepairDTO.getRepairId(),
                ordersRepairDTO.getRepairPrice()
        );
    }

    @Override
    public ArrayList<OrdersDTO> getAllOrders() throws SQLException {
        ArrayList<OrdersDTO> ordersDTOs = new ArrayList<>();
        ArrayList<Orders> orders = ordersDAO.getAll();

        for (Orders order : orders) {
            OrdersDTO orderDTO = new OrdersDTO(
                    order.getOrderId(),
                    order.getCustomerId(),
                    order.getEmployeeId(),
                    order.getOrderDate(),
                    order.getOrderTime(),
                    order.getType(),
                    order.getOrderAmount(),
                    order.getOrderStatus(),
                    getAllOrderItems(order.getOrdersItems()),
                    getAllOrderRepairs(order.getOrdersRepairs())
            );
            ordersDTOs.add(orderDTO);
        }
        return ordersDTOs;
    }

    @Override
    public ArrayList<OrdersItemDTO> getAllOrderItems(ArrayList<OrdersItem> ordersItems) throws SQLException {
        ArrayList<OrdersItemDTO> ordersItemDTOs = new ArrayList<>();
        for (OrdersItem ordersItem : ordersItems) {
            OrdersItemDTO ordersItemDTO = new OrdersItemDTO(
                    ordersItem.getOrderId(),
                    ordersItem.getItemId(),
                    ordersItem.getQuantity(),
                    ordersItem.getPrice()
            );
            ordersItemDTOs.add(ordersItemDTO);
        }

        return ordersItemDTOs;
    }

    @Override
    public ArrayList<OrdersRepairDTO> getAllOrderRepairs(ArrayList<OrdersRepair> ordersRepairs) throws SQLException {
        ArrayList<OrdersRepairDTO> ordersRepairDTOS = new ArrayList<>();
        for (OrdersRepair ordersRepair : ordersRepairs) {
            OrdersRepairDTO ordersRepairDTO = new OrdersRepairDTO(
                    ordersRepair.getOrderId(),
                    ordersRepair.getRepairId(),
                    ordersRepair.getRepairPrice()
            );
            ordersRepairDTOS.add(ordersRepairDTO);
        }

        return ordersRepairDTOS;
    }

    @Override
    public ArrayList<String> getAllOrdersIds() throws SQLException {
        return ordersDAO.getAllOrdersIds();
    }

    @Override
    public ArrayList<String> getAllOrdersIdsPaid() throws SQLException {
        return ordersDAO.getAllOrdersIdsPaid();
    }

    @Override
    public String findOrdersCustomer(String selectedOrdersId) throws SQLException {
        return ordersDAO.findOrdersCustomer(selectedOrdersId);
    }

    @Override
    public String findOrdersEmployee(String selectedOrdersId) throws SQLException {
        return ordersDAO.findOrdersEmployee(selectedOrdersId);
    }

    @Override
    public String findOrdersPrice(String selectedOrdersId) throws SQLException {
        return ordersDAO.findOrdersPrice(selectedOrdersId);
    }

    @Override
    public String findOrdersDate(String selectedOrdersId) throws SQLException {
        return ordersDAO.findOrdersDate(selectedOrdersId);
    }

    @Override
    public boolean updateOrdersPaid(String ordersId) throws SQLException {
        return ordersDAO.updateOrdersPaid(ordersId);
    }

    @Override
    public boolean updateOrdersPending(String ordersId) throws SQLException {
        return ordersDAO.updateOrdersPending(ordersId);
    }
}
