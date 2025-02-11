package lk.ijse.gdse.pcstore.bo.custom.impl;

import lk.ijse.gdse.pcstore.bo.custom.CustomerBO;
import lk.ijse.gdse.pcstore.dao.DAOFactory;
import lk.ijse.gdse.pcstore.dao.custom.CustomerDAO;
import lk.ijse.gdse.pcstore.dto.CustomerDTO;
import lk.ijse.gdse.pcstore.entity.Customer;

import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerBOImpl implements CustomerBO {

    CustomerDAO customerDAO = (CustomerDAO) DAOFactory.getInstance().getDAO(DAOFactory.DAOType.CUSTOMER);

    @Override
    public String getNextId() throws SQLException {
        return customerDAO.getNextId();
    }

    @Override
    public ArrayList<CustomerDTO> getAll() throws SQLException {
        ArrayList<CustomerDTO> customerDTOs = new ArrayList<>();
        ArrayList<Customer> customers = customerDAO.getAll();
        for (Customer customer : customers) {
            customerDTOs.add(new CustomerDTO(customer.getCustomerId(), customer.getCustomerName(), customer.getCustomerAddress(), customer.getCustomerNic(), customer.getCustomerPhone(), customer.getCustomerEmail()));
        }
        return customerDTOs;
    }

    @Override
    public boolean save(CustomerDTO customerDTO) throws SQLException {
        return customerDAO.save(new Customer(customerDTO.getCustomerId(), customerDTO.getCustomerName(), customerDTO.getCustomerAddress(), customerDTO.getCustomerNic(), customerDTO.getCustomerPhone(), customerDTO.getCustomerEmail()));
    }

    @Override
    public boolean update(CustomerDTO customerDTO) throws SQLException {
        return customerDAO.update(new Customer(customerDTO.getCustomerId(), customerDTO.getCustomerName(), customerDTO.getCustomerAddress(), customerDTO.getCustomerNic(), customerDTO.getCustomerPhone(), customerDTO.getCustomerEmail()));
    }

    @Override
    public boolean delete(String id) throws SQLException {
        return customerDAO.delete(id);
    }

    @Override
    public ArrayList<String> getAllIds() throws SQLException {
        return customerDAO.getAllIds();
    }

    @Override
    public ArrayList<String> getAllNames() throws SQLException {
        return customerDAO.getAllNames();
    }

    @Override
    public CustomerDTO findById(String id) throws SQLException {
        Customer customer = customerDAO.findById(id);
        return new CustomerDTO(customer.getCustomerId(), customer.getCustomerName(), customer.getCustomerAddress(), customer.getCustomerNic(), customer.getCustomerPhone(), customer.getCustomerEmail());
    }

    @Override
    public CustomerDTO findByName(String name) throws SQLException {
        Customer customer = customerDAO.findByName(name);
        return new CustomerDTO(customer.getCustomerId(), customer.getCustomerName(), customer.getCustomerAddress(), customer.getCustomerNic(), customer.getCustomerPhone(), customer.getCustomerEmail());
    }
}
