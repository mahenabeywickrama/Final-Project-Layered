package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.SupplierDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierModel {
    public String getNextSupplierId() throws SQLException {
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

    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into supplier values (?,?,?,?,?,?)",
                supplierDTO.getSupplierId(),
                supplierDTO.getSupplierName(),
                supplierDTO.getSupplierAddress(),
                supplierDTO.getSupplierNic(),
                supplierDTO.getSupplierPhone(),
                supplierDTO.getSupplierEmail()
        );
    }

    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from supplier");

        ArrayList<SupplierDTO> supplierDTOS = new ArrayList<>();

        while (rst.next()) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            supplierDTOS.add(supplierDTO);
        }
        return supplierDTOS;
    }

    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException {
        return SQLUtil.execute(
                "update supplier set name=?, address = ?, nic=?, phone=?, email=? where supplier_id=?",
                supplierDTO.getSupplierName(),
                supplierDTO.getSupplierAddress(),
                supplierDTO.getSupplierNic(),
                supplierDTO.getSupplierPhone(),
                supplierDTO.getSupplierEmail(),
                supplierDTO.getSupplierId()
        );
    }

    public boolean deleteSupplier(String customerId) throws SQLException {
        return SQLUtil.execute("delete from supplier where supplier_id=?", customerId);
    }

    public ArrayList<String> getAllSupplierIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select supplier_id from supplier");

        ArrayList<String> supplierIds = new ArrayList<>();

        while (rst.next()) {
            supplierIds.add(rst.getString(1));
        }

        return supplierIds;
    }

    public ArrayList<String> getAllSupplierNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select name from supplier");

        ArrayList<String> supplierNames = new ArrayList<>();

        while (rst.next()) {
            supplierNames.add(rst.getString(1));
        }

        return supplierNames;
    }

    public SupplierDTO findById(String selectedSupId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from supplier where supplier_id = ?", selectedSupId);

        if (rst.next()) {
            return new SupplierDTO(
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

    public SupplierDTO findByName(String selectedSupName) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from supplier where name = ?", selectedSupName);

        if (rst.next()) {
            return new SupplierDTO(
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
