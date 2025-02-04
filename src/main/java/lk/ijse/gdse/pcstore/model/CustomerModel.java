package lk.ijse.gdse.pcstore.model;

import lk.ijse.gdse.pcstore.dto.CustomerDTO;
import lk.ijse.gdse.pcstore.dao.SQLUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerModel {
    public String getNextCustomerId() throws SQLException {
        ResultSet rst = SQLUtil.execute("select customer_id from customer order by customer_id desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("C%03d", newIdIndex);
        }

        return "C001";
    }

    public boolean saveCustomer(CustomerDTO customerDTO) throws SQLException {
        return SQLUtil.execute(
                "insert into customer values (?,?,?,?,?,?)",
                customerDTO.getCustomerId(),
                customerDTO.getCustomerName(),
                customerDTO.getCustomerAddress(),
                customerDTO.getCustomerNic(),
                customerDTO.getCustomerPhone(),
                customerDTO.getCustomerEmail()
        );
    }

    public ArrayList<CustomerDTO> getAllCustomers() throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from customer");

        ArrayList<CustomerDTO> customerDTOS = new ArrayList<>();

        while (rst.next()) {
            CustomerDTO customerDTO = new CustomerDTO(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getString(4),
                    rst.getString(5),
                    rst.getString(6)
            );
            customerDTOS.add(customerDTO);
        }
        return customerDTOS;
    }

    public boolean updateCustomer(CustomerDTO customerDTO) throws SQLException {
        return SQLUtil.execute(
                "update customer set name=?, address = ?, nic=?, phone=?, email=? where customer_id=?",
                customerDTO.getCustomerName(),
                customerDTO.getCustomerAddress(),
                customerDTO.getCustomerNic(),
                customerDTO.getCustomerPhone(),
                customerDTO.getCustomerEmail(),
                customerDTO.getCustomerId()
        );
    }

    public boolean deleteCustomer(String customerId) throws SQLException {
        return SQLUtil.execute("delete from customer where customer_id=?", customerId);
    }

    public ArrayList<String> getAllCustomerIds() throws SQLException {
        ResultSet rst = SQLUtil.execute("select customer_id from customer");

        ArrayList<String> customerIds = new ArrayList<>();

        while (rst.next()) {
            customerIds.add(rst.getString(1));
        }

        return customerIds;
    }

    public ArrayList<String> getAllCustomerNames() throws SQLException {
        ResultSet rst = SQLUtil.execute("select name from customer");

        ArrayList<String> customerNames = new ArrayList<>();

        while (rst.next()) {
            customerNames.add(rst.getString(1));
        }

        return customerNames;
    }

    public CustomerDTO findById(String selectedCusId) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from customer where customer_id = ?", selectedCusId);

        if (rst.next()) {
            return new CustomerDTO(
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

    public CustomerDTO findByName(String selectedCusName) throws SQLException {
        ResultSet rst = SQLUtil.execute("select * from customer where name = ?", selectedCusName);

        if (rst.next()) {
            return new CustomerDTO(
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
