package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.SuppliesDAO;
import lk.ijse.gdse.pcstore.entity.Supplies;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SuppliesDAOImpl implements SuppliesDAO {
    @Override
    public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select supplies_id from supplies order by supplies_id desc limit 1");
        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("S%03d", newIdIndex);
        }

        return "S001";
    }

    @Override
    public ArrayList<Supplies> getAll() throws SQLException {
        return null;
    }

    @Override
    public boolean save(Supplies dto) throws SQLException {
        return false;
    }

    @Override
    public boolean update(Supplies dto) throws SQLException {
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
    public Supplies findById(String id) throws SQLException {
        return null;
    }

    @Override
    public Supplies findByName(String name) throws SQLException {
        return null;
    }
}
