package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.SupplierBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.SupplierDAO;
import lk.ijse.gdse.pcstore.dto.SupplierDTO;
import lk.ijse.gdse.pcstore.entity.Supplier;

import java.sql.SQLException;
import java.util.ArrayList;

public class SupplierBOImpl implements SupplierBO {

    SupplierDAO supplierDAO = (SupplierDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.SUPPLIER);

    @Override
    public String getNextSupplierId() throws SQLException {
        return supplierDAO.getNextId();
    }

    @Override
    public boolean saveSupplier(SupplierDTO supplierDTO) throws SQLException {
        return supplierDAO.save(new Supplier(supplierDTO.getSupplierId(), supplierDTO.getSupplierName(), supplierDTO.getSupplierAddress(), supplierDTO.getSupplierNic(), supplierDTO.getSupplierPhone(), supplierDTO.getSupplierEmail()));
    }

    @Override
    public ArrayList<SupplierDTO> getAllSuppliers() throws SQLException {
        ArrayList<SupplierDTO> supplierDTOs = new ArrayList<>();
        ArrayList<Supplier> suppliers = supplierDAO.getAll();

        for (Supplier supplier : suppliers) {
            SupplierDTO supplierDTO = new SupplierDTO(
                    supplier.getSupplierId(),
                    supplier.getSupplierName(),
                    supplier.getSupplierAddress(),
                    supplier.getSupplierNic(),
                    supplier.getSupplierPhone(),
                    supplier.getSupplierEmail()
            );
            supplierDTOs.add(supplierDTO);
        }

        return supplierDTOs;
    }

    @Override
    public boolean updateSupplier(SupplierDTO supplierDTO) throws SQLException {
        return supplierDAO.update(new Supplier(supplierDTO.getSupplierId(), supplierDTO.getSupplierName(), supplierDTO.getSupplierAddress(), supplierDTO.getSupplierNic(), supplierDTO.getSupplierPhone(), supplierDTO.getSupplierEmail()));
    }

    @Override
    public boolean deleteSupplier(String customerId) throws SQLException {
        return supplierDAO.delete(customerId);
    }

    @Override
    public ArrayList<String> getAllSupplierIds() throws SQLException {
        return supplierDAO.getAllIds();
    }

    @Override
    public ArrayList<String> getAllSupplierNames() throws SQLException {
        return supplierDAO.getAllNames();
    }

    @Override
    public SupplierDTO findById(String selectedSupId) throws SQLException {
        Supplier supplier = supplierDAO.findById(selectedSupId);
        return new SupplierDTO(supplier.getSupplierId(), supplier.getSupplierName(), supplier.getSupplierAddress(), supplier.getSupplierNic(), supplier.getSupplierPhone(), supplier.getSupplierEmail());
    }

    @Override
    public SupplierDTO findByName(String selectedSupName) throws SQLException {
        Supplier supplier = supplierDAO.findByName(selectedSupName);
        return new SupplierDTO(supplier.getSupplierId(), supplier.getSupplierName(), supplier.getSupplierAddress(), supplier.getSupplierNic(), supplier.getSupplierPhone(), supplier.getSupplierEmail());
    }
}
