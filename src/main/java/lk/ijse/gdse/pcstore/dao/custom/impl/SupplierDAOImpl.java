package lk.ijse.gdse.pcstore.dao.custom.impl;

import lk.ijse.gdse.pcstore.dao.SQLUtil;
import lk.ijse.gdse.pcstore.dao.custom.SupplierDAO;
import lk.ijse.gdse.pcstore.dto.SupplierDTO;
import lk.ijse.gdse.pcstore.entity.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierDAOImpl implements SupplierDAO {
    @Override
    public String getNextId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select supplier_id from supplier order by supplier_id desc limit 1");

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
    public ArrayList<Supplier> getAll() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from supplier");

        ArrayList<Supplier> suppliers = new ArrayList<>();

        while (rst.next()) {
            Supplier supplier = new Supplier(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            suppliers.add(supplier);
        }
        return suppliers;
    }

    @Override
    public boolean save(Supplier supplier) throws SQLException {
        return SQLUtil.execute(
                "insert into supplier values (?,?,?,?,?,?)",
                supplier.getSupplierId(),
                supplier.getSupplierName(),
                supplier.getSupplierAddress(),
                supplier.getSupplierNic(),
                supplier.getSupplierPhone(),
                supplier.getSupplierEmail()
        );
    }

    @Override
    public boolean update(Supplier supplier) throws SQLException {
        return SQLUtil.execute(
                "update supplier set name=?, address = ?, nic=?, phone=?, email=? where supplier_id=?",
                supplier.getSupplierName(),
                supplier.getSupplierAddress(),
                supplier.getSupplierNic(),
                supplier.getSupplierPhone(),
                supplier.getSupplierEmail(),
                supplier.getSupplierId()
        );
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return SQLUtil.execute("delete from supplier where supplier_id=?", id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select supplier_id from supplier");

        ArrayList<String> supplierIds = new ArrayList<>();

        while (rst.next()) {
            supplierIds.add(rst.getString(1));
        }

        return supplierIds;
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select name from supplier");

        ArrayList<String> supplierNames = new ArrayList<>();

        while (rst.next()) {
            supplierNames.add(rst.getString(1));
        }

        return supplierNames;
    }

    @Override
    public Supplier findById(String id) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from supplier where supplier_id = ?", id);

        if (rst.next()) {
            return new Supplier(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        }
        return null;
    }

    @Override
    public Supplier findByName(String name) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from supplier where name = ?", name);

        if (rst.next()) {
            return new Supplier(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
        }
        return null;
    }
}
