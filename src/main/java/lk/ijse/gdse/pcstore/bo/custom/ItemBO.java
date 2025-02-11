package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.ItemDTO;
import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;
import lk.ijse.gdse.pcstore.dto.ReplacementDTO;
import lk.ijse.gdse.pcstore.dto.SuppliesItemDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemBO extends SuperBO {
    String getNextId() throws SQLException;
    ArrayList<ItemDTO> getAll() throws SQLException;
    boolean save(ItemDTO dto) throws SQLException;
    boolean update(ItemDTO dto) throws SQLException;
    boolean delete(String id) throws SQLException;
    ArrayList<String> getAllIds() throws SQLException;
    ArrayList<String> getAllNames() throws SQLException;
    ItemDTO findById(String id) throws SQLException;
    ItemDTO findByName(String name) throws SQLException;
    boolean reduceQty(OrdersItemDTO ordersItemDTO) throws SQLException;
    boolean reduceQtyByReplacement(ReplacementDTO replacementDTO) throws SQLException;
    boolean increaseQtyByReplacement(ReplacementDTO replacementDTO) throws SQLException;
    boolean increaseQty(SuppliesItemDTO suppliesItemDTO) throws SQLException;
}
