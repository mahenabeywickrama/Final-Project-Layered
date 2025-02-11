package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.ItemBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.ItemDAO;
import lk.ijse.gdse.pcstore.dto.*;
import lk.ijse.gdse.pcstore.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public class ItemBOImpl implements ItemBO {

    ItemDAO itemDAO = (ItemDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.ITEM);

    @Override
    public String getNextId() throws SQLException {
        return itemDAO.getNextId();
    }

    @Override
    public ArrayList<ItemDTO> getAll() throws SQLException {
        ArrayList<ItemDTO> customerDTOs = new ArrayList<>();
        ArrayList<Item> items = itemDAO.getAll();
        for (Item item : items) {
            customerDTOs.add(new ItemDTO(item.getItemId(), item.getCategoryId(), item.getItemName(), item.getPrice(), item.getQuantity(), item.getWarranty()));
        }
        return customerDTOs;
    }

    @Override
    public boolean save(ItemDTO itemDTO) throws SQLException {
        return itemDAO.save(new Item(itemDTO.getItemId(), itemDTO.getCategoryId(), itemDTO.getItemName(), itemDTO.getPrice(), itemDTO.getQuantity(), itemDTO.getWarranty()));
    }

    @Override
    public boolean update(ItemDTO itemDTO) throws SQLException {
        return itemDAO.update(new Item(itemDTO.getItemId(), itemDTO.getCategoryId(), itemDTO.getItemName(), itemDTO.getPrice(), itemDTO.getQuantity(), itemDTO.getWarranty()));
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return itemDAO.delete(id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return itemDAO.getAllIds();
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return itemDAO.getAllNames();
    }

    @Override
    public ItemDTO findById(String id) throws SQLException {
        Item item = itemDAO.findById(id);
        return new ItemDTO(item.getItemId(), item.getCategoryId(), item.getItemName(), item.getPrice(), item.getQuantity(), item.getWarranty());
    }

    @Override
    public ItemDTO findByName(String name) throws SQLException {
        Item item = itemDAO.findByName(name);
        return new ItemDTO(item.getItemId(), item.getCategoryId(), item.getItemName(), item.getPrice(), item.getQuantity(), item.getWarranty());
    }

    public boolean reduceQty(OrdersItemDTO ordersItemDTO) throws SQLException {
        return SQLUtil.execute(
                "update item set qty_on_stock = qty_on_stock - ? where item_id = ?",
                ordersItemDTO.getQuantity(),
                ordersItemDTO.getItemId()
        );
    }

    public boolean reduceQtyByReplacement(ReplacementDTO replacementDTO) throws SQLException {
        return SQLUtil.execute(
                "update item set qty_on_stock = qty_on_stock - ? where item_id = ?",
                replacementDTO.getQty(),
                replacementDTO.getReplacedItemId()
        );
    }

    public boolean increaseQtyByReplacement(ReplacementDTO replacementDTO) throws SQLException {
        return SQLUtil.execute(
                "update item set qty_on_stock = qty_on_stock + ? where item_id = ?",
                replacementDTO.getQty(),
                replacementDTO.getReplacedItemId()
        );
    }

    public boolean increaseQty(SuppliesItemDTO suppliesItemDTO) throws SQLException {
        return SQLUtil.execute(
                "update item set qty_on_stock = qty_on_stock + ? where item_id = ?",
                suppliesItemDTO.getQuantity(),
                suppliesItemDTO.getItemId()
        );
    }
}
