package lk.ijse.gdse.pcstore.bo;

import lk.ijse.gdse.pcstore.bo.custom.impl.*;
import lk.ijse.gdse.pcstore.dao.custom.impl.*;

public class BOFactory {
    private static BOFactory boFactory;
    private BOFactory() {}
    public static BOFactory getInstance() {
        return boFactory==null?boFactory=new BOFactory():boFactory;
    }
    public enum BOType {
        CATEGORY, CUSTOMER, EMPLOYEE, ITEM, LOGIN_HISTORY, ORDERS, ORDERS_ITEM, ORDERS_REPAIR, PAYMENT, REPAIR, REPLACEMENT, SUPPLIER, SUPPLIES, SUPPLIES_ITEM, USER
    }
    public SuperBO getBO(BOType type) {
        switch (type) {
            case CATEGORY:
                return new CategoryBOImpl();
            case CUSTOMER:
                return new CustomerBOImpl();
            case EMPLOYEE:
                return new EmployeeBOImpl();
            case ITEM:
                return new ItemBOImpl();
            case LOGIN_HISTORY:
                return new LoginHistoryBOImpl();
            case ORDERS:
                return new OrdersBOImpl();
            case ORDERS_ITEM:
                return new OrdersItemBOImpl();
            case ORDERS_REPAIR:
                return new OrdersRepairBOImpl();
            case PAYMENT:
                return new PaymentBOImpl();
            case REPAIR:
                return new RepairBOImpl();
            case REPLACEMENT:
                return new ReplacementBOImpl();
            case SUPPLIER:
                return new SupplierBOImpl();
            case SUPPLIES:
                return new SuppliesBOImpl();
            case SUPPLIES_ITEM:
                return new SuppliesItemBOImpl();
            case USER:
                return new UserBOImpl();
            default:
                return null;
        }
    }
}
