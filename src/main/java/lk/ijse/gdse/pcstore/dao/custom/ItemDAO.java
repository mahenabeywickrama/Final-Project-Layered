package lk.ijse.gdse.pcstore.dao.custom;

import lk.ijse.gdse.pcstore.dao.CrudDAO;
import lk.ijse.gdse.pcstore.entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;

public interface ItemDAO extends CrudDAO<Item> {
    ArrayList<String> getAllItemNamesForCategory(String categoryId) throws SQLException;
}
