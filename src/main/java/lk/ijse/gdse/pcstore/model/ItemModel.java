package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.ItemDTO;
import lk.ijse.gdse.pcstore.dto.OrdersItemDTO;
import lk.ijse.gdse.pcstore.dto.ReplacementDTO;
import lk.ijse.gdse.pcstore.dto.SuppliesItemDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ItemModel {
    public String getNextItemId() throws SQLException {
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

    public boolean saveItem(ItemDTO itemDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into item values (?,?,?,?,?,?)",
                itemDTO.getItemId(),
                itemDTO.getCategoryId(),
                itemDTO.getItemName(),
                itemDTO.getPrice(),
                itemDTO.getQuantity(),
                itemDTO.getWarranty()
        );
    }

    public ArrayList<ItemDTO> getAllItems() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from item");

        ArrayList<ItemDTO> itemDTOS = new ArrayList<>();

        while (rst.next()) {
            ItemDTO itemDTO = new ItemDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    Double.parseDouble(rst.getString(4)),
                    Integer.parseInt(rst.getString(5)),
                    Integer.parseInt(rst.getString(6))
            );
            itemDTOS.add(itemDTO);
        }
        return itemDTOS;
    }

    public boolean updateItem(ItemDTO itemDTO) throws SQLException {
        return SQLUtil.execute(
                "update item set category_id=?, description=?, unit_price=?, qty_on_stock=?, warranty=? where item_id=?",
                itemDTO.getCategoryId(),
                itemDTO.getItemName(),
                itemDTO.getPrice(),
                itemDTO.getQuantity(),
                itemDTO.getWarranty(),
                itemDTO.getItemId()
        );
    }

    public boolean deleteItem(String itemId) throws SQLException {
        return SQLUtil.execute("delete from item where item_id=?", itemId);
    }

    public ArrayList<String> getAllItemIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select item_id from item");
        ArrayList<String> itemIds = new ArrayList<>();

        while (rst.next()) {
            itemIds.add(rst.getString(1));
        }
        return itemIds;
    }

    public ArrayList<String> getAllItemNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select description from item");
        ArrayList<String> itemNames = new ArrayList<>();

        while (rst.next()) {
            itemNames.add(rst.getString(1));
        }
        return itemNames;
    }

    public ArrayList<String> getAllItemNamesForCategory(String categoryId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select description from item where category_id=?", categoryId);
        ArrayList<String> itemNames = new ArrayList<>();

        while (rst.next()) {
            itemNames.add(rst.getString(1));
        }
        return itemNames;
    }

    public ItemDTO findById(String selectedItemId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from item where item_id=?", selectedItemId);

        if (rst.next()) {
            return new ItemDTO(
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

    public ItemDTO findByName(String selectedItemName) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from item where description=?", selectedItemName);

        if (rst.next()) {
            return new ItemDTO(
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
