package lk.ijse.gdse.pcstore.dao;

import lk.ijse.gdse.pcstore.dao.custom.impl.*;

public class DAOFactory {
    private static DAOFactory daoFactory;
    private DAOFactory() {
    }
    public static DAOFactory getInstance() {
        return daoFactory==null?daoFactory=new DAOFactory():daoFactory;
    }
    public enum DAOType {
        CATEGORY, CUSTOMER, EMPLOYEE, ITEM, LOGIN_HISTORY, ORDERS, ORDERS_ITEM, ORDERS_REPAIR, PAYMENT, REPAIR, REPLACEMENT, SUPPLIER, SUPPLIES, SUPPLIES_ITEM, USER
    }
    public SuperDAO getDAO(DAOType type) {
        switch (type) {
            case CATEGORY:
                return new CategoryDAOImpl();
            case CUSTOMER:
                return new CustomerDAOImpl();
            case EMPLOYEE:
                return new EmployeeDAOImpl();
            case ITEM:
                return new ItemDAOImpl();
            case LOGIN_HISTORY:
                return new LoginHistoryDAOImpl();
            case ORDERS:
                return new OrdersDAOImpl();
            case ORDERS_ITEM:
                return new OrdersItemDAOImpl();
            case ORDERS_REPAIR:
                return new OrdersRepairDAOImpl();
            case PAYMENT:
                return new PaymentDAOImpl();
            case REPAIR:
                return new RepairDAOImpl();
            case REPLACEMENT:
                return new ReplacementDAOImpl();
            case SUPPLIER:
                return new SupplierDAOImpl();
            case SUPPLIES:
                return new SuppliesDAOImpl();
            case SUPPLIES_ITEM:
                return new SuppliesItemDAOImpl();
            case USER:
                return new UserDAOImpl();
            default:
                return null;
        }
    }
}
