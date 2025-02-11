package lk.ijse.gdse.pcstore.bo.custom;

import lk.ijse.gdse.pcstore.bo.SuperBO;
import lk.ijse.gdse.pcstore.dto.SupplierDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SupplierBO extends SuperBO {
    String getNextSupplierId() throws SQLException;
    boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException;
    ArrayList<SupplierDTO> getAllSuppliers() throws SQLException;
    boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException;
    boolean deleteSupplier(String customerId) throws SQLException;
    ArrayList<String> getAllSupplierIds() throws SQLException;
    ArrayList<String> getAllSupplierNames() throws SQLException;
    SupplierDTO findById(String selectedSupId) throws SQLException;
    SupplierDTO findByName(String selectedSupName) throws SQLException;
}
