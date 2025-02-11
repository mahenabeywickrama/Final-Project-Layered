package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.ItemDAO;
import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;
import lk.ijse.gdse.pcstore.dto.ReplacementDTO;
import lk.ijse.gdse.pcstore.dto.SuppliesItemDTO;
import lk.ijse.gdse.pcstore.entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemDAOImpl implements ItemDAO {
    @Override
    public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select item_id from item order by item_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("I%03d", newIdIndex);
        }
        return "I001";
    }

    @Override
    public ArrayList<Item> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from item");

        ArrayList<Item> items = new ArrayList<>();

        while (rst.next()) {
            Item item = new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    Double.parseDouble(rst.getString(4)),
                    Integer.parseInt(rst.getString(5)),
                    Integer.parseInt(rst.getString(6))
            );
            items.add(item);
        }
        return items;
    }

    @Override
    public boolean save(Item item) throws SQLException {
        return SQLUtil.execute(
                "insert into item values (?,?,?,?,?,?)",
                item.getItemId(),
                item.getCategoryId(),
                item.getItemName(),
                item.getPrice(),
                item.getQuantity(),
                item.getWarranty()
        );
    }

    @Override
    public boolean update(Item item) throws SQLException {
        return SQLUtil.execute(
                "update item set category_id=?, description=?, unit_price=?, qty_on_stock=?, warranty=? where item_id=?",
                item.getCategoryId(),
                item.getItemName(),
                item.getPrice(),
                item.getQuantity(),
                item.getWarranty(),
                item.getItemId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("delete from item where item_id=?", id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select item_id from item");
        ArrayList<String> itemIds = new ArrayList<>();

        while (rst.next()) {
            itemIds.add(rst.getString(1));
        }
        return itemIds;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select description from item");
        ArrayList<String> itemNames = new ArrayList<>();

        while (rst.next()) {
            itemNames.add(rst.getString(1));
        }
        return itemNames;
    }

    @Override
    public Item findById(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from item where item_id=?", id);

        if (rst.next()) {
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5),
                    rst.getInt(6)
            );
        }
        return null;
    }

    @Override
    public Item findByName(String name) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from item where description=?", name);

        if (rst.next()) {
            return new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4),
                    rst.getInt(5),
                    rst.getInt(6)
            );
        }
        return null;
    }

    @Override
    public ArrayList<String> getAllItemNamesForCategory(String categoryId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select description from item where category_id=?", categoryId);
        ArrayList<String> itemNames = new ArrayList<>();

        while (rst.next()) {
            itemNames.add(rst.getString(1));
        }
        return itemNames;
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

    public String getCategory(String selectedItemId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select category_id from item where item_id=?", selectedItemId);

        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }
}
